const SELLING_STATUSES = new Set([
  "active",
  "available",
  "published",
  "selling",
  "on_sale",
]);

const NOT_SELLING_STATUSES = new Set([
  "archived",
  "deleted",
  "disabled",
  "draft",
  "hidden",
  "inactive",
  "out_of_stock",
  "stopped",
  "suspended",
  "unavailable",
]);

function normalizeStatus(value) {
  return String(value || "")
    .trim()
    .toLowerCase();
}

function hasExplicitFalse(product, fields) {
  return fields.some((field) => product[field] === false);
}

function hasExplicitTrue(product, fields) {
  return fields.some((field) => product[field] === true);
}

function getFirstFiniteNumber(product, fields) {
  for (const field of fields) {
    const value = Number(product[field]);

    if (Number.isFinite(value)) {
      return value;
    }
  }

  const inventoryValue = Number(product.inventory?.stockQuantity ?? product.inventory?.quantity);
  return Number.isFinite(inventoryValue) ? inventoryValue : null;
}

function isValidImageUrl(value) {
  const imageUrl = String(value || "").trim();

  if (!imageUrl) {
    return false;
  }

  try {
    const url = new URL(imageUrl);
    return url.protocol === "http:" || url.protocol === "https:";
  } catch (_) {
    return false;
  }
}

function getImageCandidates(product) {
  const candidates = [
    product.image,
    product.imageUrl,
    product.thumbnail,
    product.thumbnailUrl,
  ];

  if (Array.isArray(product.imageUrls)) {
    candidates.push(...product.imageUrls);
  }

  if (Array.isArray(product.images)) {
    product.images.forEach((image) => {
      if (typeof image === "string") {
        candidates.push(image);
        return;
      }

      candidates.push(image?.imageUrl, image?.url);
    });
  }

  return candidates;
}

const defaultRules = [
  {
    name: "isSelling",
    test(product) {
      if (product.isDeleted === true || product.deleted === true) {
        return false;
      }

      const status = normalizeStatus(product.status || product.saleStatus);

      if (NOT_SELLING_STATUSES.has(status)) {
        return false;
      }

      if (SELLING_STATUSES.has(status)) {
        return true;
      }

      return !hasExplicitFalse(product, [
        "active",
        "available",
        "isActive",
        "isAvailable",
        "isSelling",
        "published",
        "selling",
      ]);
    },
  },
  {
    name: "inStock",
    test(product) {
      const stockQuantity = getFirstFiniteNumber(product, [
        "stockQuantity",
        "stock",
        "quantity",
        "availableQuantity",
      ]);

      return stockQuantity !== null && stockQuantity > 0;
    },
  },
  {
    name: "isVisible",
    test(product) {
      if (product.isHidden === true || product.hidden === true) {
        return false;
      }

      if (hasExplicitTrue(product, ["isVisible", "visible"])) {
        return true;
      }

      return !hasExplicitFalse(product, ["isVisible", "visible"]);
    },
  },
  {
    name: "hasValidImage",
    test(product) {
      return getImageCandidates(product).some(isValidImageUrl);
    },
  },
];

class ProductFilterService {
  static filter(candidateProducts, rules = defaultRules) {
    if (!Array.isArray(candidateProducts)) {
      return [];
    }

    return candidateProducts.filter((product) => {
      if (!product || typeof product !== "object" || Array.isArray(product)) {
        return false;
      }

      return rules.every((rule) => rule.test(product));
    });
  }
}

module.exports = ProductFilterService;
module.exports._internals = {
  defaultRules,
  getImageCandidates,
  getFirstFiniteNumber,
  isValidImageUrl,
};
