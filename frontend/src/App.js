import React, { useState, useEffect } from 'react';
import './App.css';

function App() {
  const [view, setView] = useState('home');
  const [username, setUsername] = useState('');
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [cartRefresh, setCartRefresh] = useState(0);
  const [orderSummary, setOrderSummary] = useState(null);
  const [showPayment, setShowPayment] = useState(false);

  const handleCheckout = (cart, onSuccess) => {
    setShowPayment(true);
    // onSuccess will be called after payment method is selected
    setOrderSummary({ cart, onSuccess });
  };

  const handlePayment = (method) => {
    setShowPayment(false);
    // Call backend checkout
    fetch(`/api/orders/checkout?username=${username || 'guest'}&paymentMethod=${method}`, {
      method: 'POST'
    })
      .then(res => res.json())
      .then(summary => {
        setOrderSummary(summary);
        setView('orderSummary');
        setCartRefresh(r => r + 1); // clear cart in UI
      });
  };

  return (
    <div className="container-fluid px-0 min-vh-100">
      <header>
        <nav className="navbar navbar-expand-lg mb-4 rounded-bottom shadow-sm">
          <div className="container-fluid">
            <span className="navbar-brand fw-bold">Simple Ecommerce</span>
            <div>
              <button className="btn btn-outline-light me-2" onClick={() => setView('products')}>Products</button>
              <button className="btn btn-outline-light me-2" onClick={() => setView('cart')}>Cart</button>
              {!isLoggedIn && <button className="btn btn-outline-success me-2" onClick={() => setView('login')}>Login</button>}
              {!isLoggedIn && <button className="btn btn-outline-secondary me-2" onClick={() => setView('register')}>Register</button>}
              {isLoggedIn && <span className="ms-3">Welcome, <b>{username}</b>!</span>}
              {isLoggedIn && <button className="btn btn-danger ms-3" onClick={() => { setIsLoggedIn(false); setUsername(''); setView('home'); }}>Logout</button>}
            </div>
          </div>
        </nav>
      </header>
      <main className="container main-bg">
        {view === 'home' && <div className="text-center"><h2>Welcome to the Simple Ecommerce App!</h2></div>}
        {view === 'products' && <ProductsPlaceholder onAddToCart={() => setCartRefresh(r => r + 1)} />}
        {view === 'cart' && <CartPlaceholder refresh={cartRefresh} onCheckout={handleCheckout} />}
        {view === 'login' && <LoginForm onLogin={(user) => { setUsername(user); setIsLoggedIn(true); setView('home'); }} />}
        {view === 'register' && <RegisterForm onRegister={() => setView('login')} />}
        {view === 'orderSummary' && orderSummary && <OrderSummaryPage summary={orderSummary} onBack={() => setView('products')} />}
      </main>
      {showPayment && <PaymentModal onSelect={handlePayment} onClose={() => setShowPayment(false)} />}
    </div>
  );
}

function PaymentModal({ onSelect, onClose }) {
  return (
    <div className="modal d-block" tabIndex="-1" style={{background: 'rgba(0,0,0,0.3)'}}>
      <div className="modal-dialog modal-dialog-centered">
        <div className="modal-content">
          <div className="modal-header">
            <h5 className="modal-title">Select Payment Method</h5>
            <button type="button" className="btn-close" onClick={onClose}></button>
          </div>
          <div className="modal-body text-center">
            <button className="btn btn-primary me-3" onClick={() => onSelect('UPI')}>Pay with UPI</button>
            <button className="btn btn-success" onClick={() => onSelect('COD')}>Cash on Delivery</button>
          </div>
        </div>
      </div>
    </div>
  );
}

function OrderSummaryPage({ summary, onBack }) {
  if (!summary || !summary.orderId) return <div className="alert alert-danger">{summary?.message || 'Order not found.'}</div>;
  return (
    <div className="text-center">
      <h2>Order Summary</h2>
      <div className="card mx-auto my-4" style={{maxWidth: 500}}>
        <div className="card-body">
          <h5 className="card-title">Order ID: {summary.orderId}</h5>
          <p className="mb-1"><b>User:</b> {summary.username}</p>
          <p className="mb-1"><b>Payment:</b> {summary.paymentMethod}</p>
          <p className="mb-1"><b>Total:</b> ₹{summary.total}</p>
          <p className="mb-1"><b>Products:</b> {summary.productIds && summary.productIds.join(', ')}</p>
          <div className="alert alert-success mt-3">{summary.message}</div>
        </div>
      </div>
      <button className="btn btn-primary" onClick={onBack}>Back to Products</button>
    </div>
  );
}

function ProductsPlaceholder({ onAddToCart }) {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [addMsg, setAddMsg] = useState('');

  useEffect(() => {
    setLoading(true);
    fetch('/api/products')
      .then((res) => {
        if (!res.ok) throw new Error('Failed to fetch products');
        return res.json();
      })
      .then((data) => {
        setProducts(data);
        setLoading(false);
      })
      .catch((err) => {
        setError(err.message);
        setLoading(false);
      });
  }, []);

  const handleAddToCart = (product) => {
    setAddMsg('');
    fetch('/api/cart/add', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ productId: product.id, quantity: 1 })
    })
      .then(res => res.text())
      .then(msg => {
        setAddMsg(msg);
        onAddToCart();
        setTimeout(() => setAddMsg(''), 1500);
      })
      .catch(() => setAddMsg('Failed to add to cart'));
  };

  if (loading) return <div className="text-center"><div className="spinner-border" role="status"><span className="visually-hidden">Loading...</span></div></div>;
  if (error) return <div className="alert alert-danger">Error: {error}</div>;
  if (products.length === 0) return <div className="alert alert-warning">No products found.</div>;

  return (
    <div>
      <h2>Products</h2>
      {addMsg && <div className="alert alert-success">{addMsg}</div>}
      <div className="row">
        {products.map((product) => (
          <div className="col-12 col-sm-6 col-md-4 col-lg-3 mb-4" key={product.id}>
            <div className="card h-100 shadow-sm border-0">
              <div className="card-body d-flex flex-column justify-content-between">
                <h5 className="card-title">{product.name}</h5>
                <h6 className="card-subtitle mb-2 text-muted">${product.price}</h6>
                <p className="card-text">{product.description}</p>
                <button className="btn btn-primary mt-auto" onClick={() => handleAddToCart(product)}>Add to Cart</button>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

function CartPlaceholder({ refresh, onCheckout }) {
  const [cart, setCart] = useState([]);
  const [loading, setLoading] = useState(true);
  const [msg, setMsg] = useState('');
  const [error, setError] = useState('');
  const [quantities, setQuantities] = useState({});

  useEffect(() => {
    setLoading(true);
    fetch('/api/cart')
      .then(res => res.json())
      .then(data => {
        setCart(data);
        setQuantities(Object.fromEntries(data.map(item => [item.productId, item.quantity])));
        setLoading(false);
      })
      .catch(() => {
        setCart([]);
        setLoading(false);
        setError('Failed to load cart');
      });
  }, [refresh]);

  const handleClearCart = () => {
    fetch('/api/cart/clear', { method: 'POST' })
      .then(res => res.text())
      .then(msg => {
        setMsg(msg);
        setCart([]);
      });
  };

  const handleRemove = (productId) => {
    fetch('/api/cart/remove', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ productId })
    })
      .then(res => res.text())
      .then(msg => {
        setMsg(msg);
        setCart(cart.filter(item => item.productId !== productId));
      });
  };

  const handleUpdate = (productId) => {
    const quantity = quantities[productId];
    fetch('/api/cart/update', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ productId, quantity })
    })
      .then(res => res.text())
      .then(msg => {
        setMsg(msg);
        setCart(cart.map(item => item.productId === productId ? { ...item, quantity } : item));
      });
  };

  const handleQuantityChange = (productId, value) => {
    setQuantities({ ...quantities, [productId]: Number(value) });
  };

  if (loading) return <div className="text-center"><div className="spinner-border" role="status"><span className="visually-hidden">Loading...</span></div></div>;
  return (
    <div>
      <h2>Cart</h2>
      {msg && <div className="alert alert-success">{msg}</div>}
      {error && <div className="alert alert-danger">{error}</div>}
      {cart.length === 0 ? <div className="alert alert-info">Your cart is empty.</div> : (
        <>
        <div className="table-responsive">
          <table className="table table-bordered table-striped align-middle">
            <thead className="table-primary">
              <tr>
                <th>Product ID</th>
                <th>Quantity</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {cart.map((item) => (
                <tr key={item.productId}>
                  <td>{item.productId}</td>
                  <td style={{width: 120}}>
                    <input
                      type="number"
                      min="1"
                      className="form-control"
                      value={quantities[item.productId] || 1}
                      onChange={e => handleQuantityChange(item.productId, e.target.value)}
                    />
                  </td>
                  <td style={{width: 200}}>
                    <button className="btn btn-primary me-2" onClick={() => handleUpdate(item.productId)}>Update</button>
                    <button className="btn btn-danger" onClick={() => handleRemove(item.productId)}>Remove</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        <div className="text-end mt-3">
          <button className="btn btn-success" onClick={() => onCheckout(cart)}>Checkout</button>
        </div>
        </>
      )}
      <button className="btn btn-danger mt-3" onClick={handleClearCart} disabled={cart.length === 0}>Clear Cart</button>
    </div>
  );
}

function LoginForm({ onLogin }) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleSubmit = (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    fetch('/api/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, password })
    })
      .then(res => res.text())
      .then(msg => {
        setLoading(false);
        if (msg === 'Login successful') {
          onLogin(username);
        } else {
          setError(msg);
        }
      })
      .catch(() => {
        setLoading(false);
        setError('Network error');
      });
  };

  return (
    <form onSubmit={handleSubmit} className="mx-auto mt-4 p-4 border rounded shadow-sm bg-light" style={{maxWidth: 350}}>
      <h2 className="mb-3">Login</h2>
      <div className="mb-3">
        <input type="text" className="form-control" placeholder="Username" value={username} onChange={e => setUsername(e.target.value)} required />
      </div>
      <div className="mb-3">
        <input type="password" className="form-control" placeholder="Password" value={password} onChange={e => setPassword(e.target.value)} required />
      </div>
      <button type="submit" className="btn btn-success w-100" disabled={loading}>{loading ? 'Logging in...' : 'Login'}</button>
      {error && <div className="alert alert-danger mt-3">{error}</div>}
    </form>
  );
}

function RegisterForm({ onRegister }) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleSubmit = (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    setSuccess(null);
    fetch('/api/auth/register', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, password })
    })
      .then(res => res.text())
      .then(msg => {
        setLoading(false);
        if (msg === 'Registration successful') {
          setSuccess('Registration successful! You can now log in.');
          setTimeout(() => onRegister(), 1500);
        } else {
          setError(msg);
        }
      })
      .catch(() => {
        setLoading(false);
        setError('Network error');
      });
  };

  return (
    <form onSubmit={handleSubmit} className="mx-auto mt-4 p-4 border rounded shadow-sm bg-light" style={{maxWidth: 350}}>
      <h2 className="mb-3">Register</h2>
      <div className="mb-3">
        <input type="text" className="form-control" placeholder="Username" value={username} onChange={e => setUsername(e.target.value)} required />
      </div>
      <div className="mb-3">
        <input type="password" className="form-control" placeholder="Password" value={password} onChange={e => setPassword(e.target.value)} required />
      </div>
      <button type="submit" className="btn btn-primary w-100" disabled={loading}>{loading ? 'Registering...' : 'Register'}</button>
      {error && <div className="alert alert-danger mt-3">{error}</div>}
      {success && <div className="alert alert-success mt-3">{success}</div>}
    </form>
  );
}

export default App;
