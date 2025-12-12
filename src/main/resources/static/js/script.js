async function loadAllProducts() {
    const query = `
        query {
            getAllProducts {
                id
                title
                price
                category {
                    name
                }
                user {
                    fullname
                }
            }
        }
    `;
    fetchGraphQL(query, 'getAllProducts');
}

async function loadProductsSortedByPrice() {
    const query = `
        query {
            productsSortedByPrice {
                id
                title
                price
                category {
                    name
                }
                user {
                    fullname
                }
            }
        }
    `;
    fetchGraphQL(query, 'productsSortedByPrice');
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
        const data = result.data[dataKey];
        renderTable(data);
    } catch (error) {
        console.error('Error:', error);
    }
}

function renderTable(products) {
    const tbody = document.getElementById('productTableBody');
    tbody.innerHTML = '';
    products.forEach(product => {
        const row = `
            <tr>
                <td>${product.id}</td>
                <td>${product.title}</td>
                <td>${product.price}</td>
                <td>${product.category ? product.category.name : 'N/A'}</td>
                <td>${product.user ? product.user.fullname : 'N/A'}</td>
            </tr>
        `;
        tbody.innerHTML += row;
    });
}
