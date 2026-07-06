const STOP_WORDS = new Set([
  "a",
  "an",
  "and",
  "for",
  "in",
  "of",
  "the",
  "with",
]);

const CATEGORY_ALIASES = {
  beverage: "beverage",
  beverages: "beverage",
  drink: "beverage",
  drinks: "beverage",
  soda: "beverage",
  softdrink: "beverage",
  "soft drink": "beverage",
  snack: "snack",
  snacks: "snack",
  dairy: "dairy",
  milk: "dairy",
  "personal care": "personal care",
  personalcare: "personal care",
  household: "household",
  cleaning: "household",
};

const CATEGORY_SEARCH_TERMS = {
  beverage: ["soft drink", "drink"],
  snack: ["snack"],
  dairy: ["dairy", "milk"],
  "personal care": ["personal care"],
  household: ["household"],
};

function normalizeText(value) {
  return String(value || "")
    .trim()
    .toLowerCase()
    .replace(/\s+/g, " ");
}

function normalizeCategory(value) {
  const normalized = normalizeText(value);
  return CATEGORY_ALIASES[normalized] || normalized;
}

function uniqueNonEmpty(values) {
  const seen = new Set();

  return values.filter((value) => {
    const normalized = normalizeText(value);

    if (!normalized || seen.has(normalized)) {
      return false;
    }

    seen.add(normalized);
    return true;
  });
}

function expandFromProductName(intent) {
  const productName = normalizeText(intent.productName);
  return productName ? [productName] : [];
}

function areSimilarLabels(left, right) {
  const normalize = (value) => normalizeText(value).replace(/[^a-z0-9]/g, "");

  return normalize(left) === normalize(right);
}

function expandFromBrand(intent) {
  const brand = normalizeText(intent.brand);
  const productName = normalizeText(intent.productName);

  if (!brand || areSimilarLabels(brand, productName)) {
    return [];
  }

  return [brand];
}

function expandFromProductTokens(intent) {
  const productName = normalizeText(intent.productName);
  const words = productName.split(" ").filter(Boolean);

  if (words.length < 2) {
    return [];
  }

  const trailingPhrase = words.slice(1).join(" ");
  const lastWord = words[words.length - 1];

  return uniqueNonEmpty([trailingPhrase, lastWord]).filter(
    (token) => !STOP_WORDS.has(token) && token.length >= 3
  );
}

function expandFromCategory(intent) {
  const category = normalizeCategory(intent.category);
  const terms = CATEGORY_SEARCH_TERMS[category] || [];

  return terms.length > 0 ? [terms[0]] : [];
}

const QUERY_EXPANDERS = [
  expandFromProductName,
  expandFromBrand,
  expandFromProductTokens,
  expandFromCategory,
];

function buildCategoryFilter(intent) {
  const category = normalizeCategory(intent.category);

  if (!category) {
    return [];
  }

  return [{ attribute: "category", value: category }];
}

function buildBrandFilter(intent) {
  const brand = normalizeText(intent.brand);

  if (!brand) {
    return [];
  }

  return [{ attribute: "brand", value: brand, optional: true }];
}

const FILTER_BUILDERS = [buildCategoryFilter, buildBrandFilter];

function buildQueries(intent) {
  return QUERY_EXPANDERS.reduce((queries, expander) => {
    return uniqueNonEmpty([...queries, ...expander(intent)]);
  }, []);
}

function buildFilters(intent) {
  return FILTER_BUILDERS.reduce((filters, builder) => {
    return [...filters, ...builder(intent)];
  }, []);
}

function toSearchPlan(intent) {
  const safeIntent = intent || {};

  return {
    queries: buildQueries(safeIntent),
    filters: buildFilters(safeIntent),
    category: normalizeCategory(safeIntent.category),
  };
}

class SearchPlanBuilder {
  static build(intent) {
    return toSearchPlan(intent);
  }
}

module.exports = SearchPlanBuilder;
module.exports._internals = {
  CATEGORY_ALIASES,
  CATEGORY_SEARCH_TERMS,
  QUERY_EXPANDERS,
  FILTER_BUILDERS,
  normalizeCategory,
  toSearchPlan,
};
