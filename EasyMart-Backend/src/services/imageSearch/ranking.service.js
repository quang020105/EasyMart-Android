function normalizeText(value) {
  return String(value || "")
    .trim()
    .toLowerCase();
}

function tokenize(value) {
  return normalizeText(value)
    .split(/[^a-z0-9]+/i)
    .map((token) => token.trim())
    .filter((token) => token.length > 1);
}

function containsNormalized(source, expected) {
  const normalizedSource = normalizeText(source);
  const normalizedExpected = normalizeText(expected);

  return Boolean(
    normalizedSource &&
      normalizedExpected &&
      normalizedSource.includes(normalizedExpected)
  );
}

function getSearchableText(product) {
  return [
    product.name,
    product.title,
    product.brand,
    product.category,
    product.description,
  ]
    .filter(Boolean)
    .join(" ");
}

function getPopularityValue(product) {
  const values = [
    product.popularity,
    product.popularityScore,
    product.soldQuantity,
    product.rating?.count,
    product.reviewCount,
  ]
    .map(Number)
    .filter(Number.isFinite);

  return values.length === 0 ? 0 : Math.max(...values);
}

function buildRankingContext(products) {
  const popularityValues = products.map(getPopularityValue);
  const maxPopularity = Math.max(0, ...popularityValues);

  return {
    maxPopularity,
  };
}

const defaultScoringRules = [
  {
    name: "brandMatch",
    weight: 40,
    score(product, intent) {
      if (!intent.brand) {
        return 0;
      }

      return containsNormalized(product.brand, intent.brand) ||
        containsNormalized(product.name, intent.brand) ||
        containsNormalized(product.title, intent.brand)
        ? 1
        : 0;
    },
  },
  {
    name: "categoryMatch",
    weight: 20,
    score(product, intent) {
      if (!intent.category) {
        return 0;
      }

      return containsNormalized(product.category, intent.category) ? 1 : 0;
    },
  },
  {
    name: "keywordMatch",
    weight: 20,
    score(product, intent) {
      const keywords = tokenize(intent.productName);

      if (keywords.length === 0) {
        return 0;
      }

      const searchableText = normalizeText(getSearchableText(product));
      const matchedCount = keywords.filter((keyword) =>
        searchableText.includes(keyword)
      ).length;

      return matchedCount / keywords.length;
    },
  },
  {
    name: "popularity",
    weight: 20,
    score(product, _intent, context) {
      if (!context.maxPopularity) {
        return 0;
      }

      return getPopularityValue(product) / context.maxPopularity;
    },
  },
];

function scoreProduct(product, intent, rules, context) {
  const breakdown = {};
  const totalScore = rules.reduce((score, rule) => {
    const rawRuleScore = Number(rule.score(product, intent, context));
    const normalizedRuleScore = Number.isFinite(rawRuleScore)
      ? Math.min(Math.max(rawRuleScore, 0), 1)
      : 0;
    const weightedScore = normalizedRuleScore * rule.weight;

    breakdown[rule.name] = weightedScore;
    return score + weightedScore;
  }, 0);

  return {
    score: Math.round(totalScore * 100) / 100,
    scoreBreakdown: breakdown,
  };
}

class RankingService {
  static rank(products, intent = {}, rules = defaultScoringRules) {
    if (!Array.isArray(products)) {
      return [];
    }

    const context = buildRankingContext(products);

    return products
      .map((product, index) => ({
        product,
        index,
        ...scoreProduct(product, intent, rules, context),
      }))
      .sort((left, right) => {
        if (right.score !== left.score) {
          return right.score - left.score;
        }

        return left.index - right.index;
      })
      .map(({ product, score, scoreBreakdown }) => ({
        ...product,
        score,
        scoreBreakdown,
      }));
  }
}

module.exports = RankingService;
module.exports._internals = {
  buildRankingContext,
  defaultScoringRules,
  getPopularityValue,
  scoreProduct,
  tokenize,
};
