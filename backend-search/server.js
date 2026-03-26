require('dotenv').config();
const express = require('express');
const { algoliasearch } = require('algoliasearch');
const cors = require('cors');
const crypto = require('crypto');

const app = express();
app.use(cors());
app.use(express.json());

const ALGOLIA_APP_ID = process.env.ALGOLIA_APP_ID;
const ALGOLIA_ADMIN_KEY = process.env.ALGOLIA_ADMIN_KEY;
const ALGOLIA_SEARCH_KEY = process.env.ALGOLIA_SEARCH_KEY;

if (!ALGOLIA_APP_ID || !ALGOLIA_ADMIN_KEY || !ALGOLIA_SEARCH_KEY) {
  console.error("Set ALGOLIA_APP_ID and ALGOLIA_ADMIN_KEY and ALGOLIA_SEARCH_KEY in .env");
  process.exit(1);
}

const client = algoliasearch(ALGOLIA_APP_ID, ALGOLIA_ADMIN_KEY);

function generateSecuredApiKey(searchKey, restrictions) {
    const queryString = Object.keys(restrictions)
        .sort()
        .map(key => {
            const value = Array.isArray(restrictions[key]) 
                ? restrictions[key].join(',') 
                : restrictions[key];
            return `${encodeURIComponent(key)}=${encodeURIComponent(value)}`;
        })
        .join('&');
    
    const hash = crypto
        .createHmac('sha256', searchKey)
        .update(queryString)
        .digest('hex');
    
    return Buffer.from(`${hash}${queryString}`).toString('base64');
}

app.post('/algolia/key',(req, res) => {
    try{
        const {index = 'products', ttlSeconds = 300 } = req.body;
        const restrictions = {
            restrictIndices: [index],
            validUntil: Math.floor(Date.now() / 1000) + ttlSeconds
        };

        const securedKey = generateSecuredApiKey(ALGOLIA_SEARCH_KEY, {
            restrictIndices: index,
            validUntil: Math.floor(Date.now() / 1000) + ttlSeconds
        });

        res.json({
            appId: ALGOLIA_APP_ID,
            apiKey: securedKey,
            index: index
        });
    } catch(err){
        console.error(err);
        res.status(500).send({error: 'server error'});
    }
});

const PORT = process.env.PORT || 8080;
app.listen(PORT, () => console.log(`Algolia key server running on :${PORT}`));
