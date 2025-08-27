import React from 'react'
import { Routes, Route, Navigate } from 'react-router-dom'
import Navbar from './components/Navbar'
import { AuthProvider, useAuth } from './auth'
import Home from './pages/Home'
import Login from './pages/Login'
import ListingsPage from './pages/ListingsPage'
import ListingDetail from './pages/ListingDetail'
import PostsPage from './pages/PostsPage'
import PostDetail from './pages/PostDetail'
import NewItem from './pages/NewItem'
import Profile from './pages/Profile'
import Favorites from './pages/Favorites'   // ✅ 这个组件名就叫 Favorites

function Protected({ children }) {
  const { user, loading } = useAuth()
  if (loading) return <div className="container">Loading...</div>
  if (!user) return <Navigate to="/login" replace />
  return children
}

export default function App() {
  return (
    <AuthProvider>
      <Navbar />
      <div className="container">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/rentals" element={<ListingsPage type="rental" />} />
          <Route path="/jobs" element={<ListingsPage type="job" />} />
          <Route path="/listings/:id" element={<ListingDetail />} />

          <Route path="/favorites" element={<Protected><Favorites /></Protected>} />
          <Route path="/me/favorites" element={<Navigate to="/favorites" replace />} />

          <Route path="/posts" element={<PostsPage />} />
          <Route path="/posts/:id" element={<PostDetail />} />
          <Route path="/new" element={<Protected><NewItem /></Protected>} />
          <Route path="/me" element={<Protected><Profile /></Protected>} />
        </Routes>
      </div>
      <footer>© AdelifeHub</footer>
    </AuthProvider>
  )
}
