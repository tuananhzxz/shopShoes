document.addEventListener('DOMContentLoaded', function () {
    document.querySelectorAll('.btn-add-to-cart').forEach(button => {
        button.addEventListener('click', function () {
            const productId = this.getAttribute('data-id');

            fetch('/cart/add', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ productId: productId, quantity: 1 })
            })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        alert('Product added to cart');
                    } else {
                        alert('Failed to add product to cart');
                    }
                });
        });
    });
});
