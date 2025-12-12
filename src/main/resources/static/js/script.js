document.addEventListener('DOMContentLoaded', () => {
    loadCategories();
    loadAllProducts();
});

let currentProducts = [];

async function loadCategories() {
    const query = `
        query {
            getAllCategories {
                id
                name
            }
        }
    `;
    const data = await fetchGraphQL(query, 'getAllCategories');
    if (data) {
        const filterContainer = document.getElementById('category-filters');
        data.forEach(cat => {
            const btn = document.createElement('button');
            btn.className = 'filter-btn';
            btn.textContent = cat.name;
            btn.onclick = (e) => filterByCategory(cat.id, e.target);
            filterContainer.appendChild(btn);
        });
    }
}

async function loadAllProducts() {
    const query = `
        query {
            getAllProducts {
                id
                title
                price
                description
                quantity
                category {
                    id
                    name
                }
                user {
                    fullname
                }
            }
        }
    `;
    const data = await fetchGraphQL(query, 'getAllProducts');
    if (data) {
        currentProducts = data;
        renderProducts(data);
    }
}

async function loadProductsSortedByPrice() {
    // Optimistic sorting on client side if we already have data, otherwise fetch
    if (currentProducts.length > 0) {
        const sorted = [...currentProducts].sort((a, b) => a.price - b.price);
        renderProducts(sorted);
        return;
    }

    const query = `
        query {
            productsSortedByPrice {
                id
                title
                price
                description
                quantity
                category {
                    id
                    name
                }
            }
        }
    `;
    const data = await fetchGraphQL(query, 'productsSortedByPrice');
    if (data) {
        currentProducts = data;
        renderProducts(data);
    }
}

async function filterByCategory(categoryId, btnElement) {
    // Update active button state
    document.querySelectorAll('.filter-btn').forEach(b => b.classList.remove('active'));
    btnElement.classList.add('active');

    if (categoryId === 'all') {
        loadAllProducts();
        return;
    }

    const query = `
        query {
            productsByCategory(categoryId: "${categoryId}") {
                id
                title
                price
                description
                quantity
                category {
                    id
                    name
                }
                user {
                    fullname
                }
            }
        }
    `;

    // Show loading
    document.getElementById('productGrid').innerHTML = '<div class="col-12 text-center py-5"><div class="spinner-border text-primary"></div></div>';

    const data = await fetchGraphQL(query, 'productsByCategory');
    if (data) {
        currentProducts = data;
        renderProducts(data);
    }
}

async function fetchGraphQL(query, dataKey) {
    try {
        const response = await fetch('/graphql', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
            },
            body: JSON.stringify({ query })
        });

        const result = await response.json();
        if (result.errors) {
            console.error('GraphQL Errors:', result.errors);
            alert('Error fetching data. Check console.');
            return null;
        }
        return result.data[dataKey];
    } catch (error) {
        console.error('Network Error:', error);
        return null;
    }
}

function renderProducts(products) {
    const grid = document.getElementById('productGrid');
    grid.innerHTML = '';

    if (products.length === 0) {
        grid.innerHTML = '<div class="col-12 text-center"><p class="text-muted">No products found.</p></div>';
        return;
    }

    products.forEach(product => {
        // Generate a random gradient placeholder if no image URL is real
        const cardHtml = `
            <div class="col-md-6 col-lg-4 col-xl-3">
                <div class="product-card h-100">
                    <div class="product-img-wrap">
                         <i class="fa-solid fa-box-open fa-3x text-secondary"></i>
                         <div class="badge-stock">Qty: ${product.quantity}</div>
                    </div>
                    <div class="card-body d-flex flex-column">
                        <span class="product-category">${product.category ? product.category.name : 'Uncategorized'}</span>
                        <h5 class="product-title">${product.title}</h5>
                        <p class="text-muted small mb-3 flex-grow-1">${product.description || 'No description available.'}</p>
                        <div class="d-flex justify-content-between align-items-center mt-3">
                            <span class="product-price">$${product.price}</span>
                             <button class="btn btn-sm btn-outline-primary rounded-circle" onclick="alert('Added to cart: ${product.title}')">
                                <i class="fa-solid fa-plus"></i>
                            </button>
                        </div>
                        <button class="btn-add-cart mt-3" onclick="alert('Added to cart: ${product.title}')">
                            <i class="fa-solid fa-cart-plus me-2"></i> Add to Cart
                        </button>
                        <small class="text-muted mt-2" style="font-size: 0.7rem"><i class="fa-solid fa-user"></i> Seller: ${product.user ? product.user.fullname : 'System'}</small>
                    </div>
                </div>
            </div>
        `;
        grid.innerHTML += cardHtml;
    });
}
