// Product dataset
// Product dataset with working images
const products = [
  { id: 1, name: "Laptop", category: "electronics", price: "$800", img: "https://placehold.co/200x150?text=Laptop" },
  { id: 2, name: "Smartphone", category: "electronics", price: "$500", img: "https://placehold.co/200x150?text=Phone" },
  { id: 3, name: "Headphones", category: "electronics", price: "$120", img: "https://placehold.co/200x150?text=Headphones" },
  
  { id: 4, name: "T-Shirt", category: "clothing", price: "$20", img: "https://placehold.co/200x150?text=T-Shirt" },
  { id: 5, name: "Jeans", category: "clothing", price: "$40", img: "https://placehold.co/200x150?text=Jeans" },
  { id: 6, name: "Jacket", category: "clothing", price: "$60", img: "https://placehold.co/200x150?text=Jacket" },
  
  { id: 7, name: "Novel", category: "books", price: "$15", img: "https://placehold.co/200x150?text=Novel" },
  { id: 8, name: "Notebook", category: "books", price: "$5", img: "https://placehold.co/200x150?text=Notebook" },
  { id: 9, name: "Comics", category: "books", price: "$12", img: "https://placehold.co/200x150?text=Comics" },
  
  { id: 10, name: "Sofa", category: "furniture", price: "$400", img: "https://placehold.co/200x150?text=Sofa" },
  { id: 11, name: "Chair", category: "furniture", price: "$70", img: "https://placehold.co/200x150?text=Chair" },
  { id: 12, name: "Table", category: "furniture", price: "$150", img: "https://placehold.co/200x150?text=Table" },
];


// DOM elements
const categoryFilter = document.getElementById("categoryFilter");
const productList = document.getElementById("productList");

// Render products
function renderProducts(items) {
  if (items.length === 0) {
    productList.innerHTML = `<p class="message">❌ No products found.</p>`;
    return;
  }
  productList.innerHTML = items.map(product => `
    <div class="product">
      <img src="${product.img}" alt="${product.name}">
      <h3>${product.name}</h3>
      <p>Category: ${product.category}</p>
      <span>${product.price}</span>
    </div>
  `).join("");
}

// Initial render
renderProducts(products);

// Filter event
categoryFilter.addEventListener("change", () => {
  const value = categoryFilter.value;
  if (value === "all") {
    renderProducts(products);
  } else {
    const filtered = products.filter(p => p.category === value);
    renderProducts(filtered);
  }
});