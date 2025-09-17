// Sample Product Data
const products = [
  { id: 1, title: "Smartphone", category: "electronics" },
  { id: 2, title: "Laptop", category: "electronics" },
  { id: 3, title: "Jeans", category: "clothing" },
  { id: 4, title: "Bookshelf", category: "furniture" },
  { id: 5, title: "T-shirt", category: "clothing" },
  { id: 6, title: "Novel: Sci-Fi", category: "books" },
  { id: 7, title: "Desk Lamp", category: "electronics" },
  { id: 8, title: "Chair", category: "furniture" },
  { id: 9, title: "Notebook", category: "books" },
];

// DOM Elements
const productList = document.getElementById("productList");
const categoryFilter = document.getElementById("categoryFilter");

// Render products
function renderProducts(filteredProducts) {
  productList.innerHTML = ""; // Clear previous items

  if (filteredProducts.length === 0) {
    productList.innerHTML = "<p>No products found.</p>";
    return;
  }

  filteredProducts.forEach(product => {
    const card = document.createElement("div");
    card.className = "product-card";
    card.innerHTML = `
      <div class="product-title">${product.title}</div>
      <div class="product-category">${product.category}</div>
    `;
    productList.appendChild(card);
  });
}

// Filter logic
function filterProducts() {
  const selectedCategory = categoryFilter.value;
  if (selectedCategory === "all") {
    renderProducts(products);
  } else {
    const filtered = products.filter(product => product.category === selectedCategory);
    renderProducts(filtered);
  }
}

// Event listener
categoryFilter.addEventListener("change", filterProducts);

// Initial load
renderProducts(products);
