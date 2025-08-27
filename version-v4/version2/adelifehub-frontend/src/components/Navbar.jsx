import React from 'react'
import { Link, NavLink, useNavigate } from 'react-router-dom'
import { useAuth } from '../auth'
import Avatar from './Avatar'

export default function Navbar(){
  const { user, logout } = useAuth()
  const nav = useNavigate()
  return (
    <div className="nav">
      <div className="nav-inner">
        <Link to="/" className="row" style={{gap:8,fontWeight:800}}>AdelifeHub</Link>
        <NavLink to="/" end className={({isActive})=>isActive?'active':''}>Home</NavLink>
        <NavLink to="/rentals" className={({isActive})=>isActive?'active':''}>Rentals</NavLink>
        <NavLink to="/jobs" className={({isActive})=>isActive?'active':''}>Jobs</NavLink>
        <NavLink to="/posts" className={({isActive})=>isActive?'active':''}>Posts</NavLink>
        <div className="spacer"></div>
        {user ? (
          <div className="row" style={{gap:8}}>
            <button className="btn" onClick={()=>nav('/new')}>Publish</button>
            <Link to="/me" title="Profile"><Avatar url={user.avatarUrl} name={user.nickname || user.username} /></Link>
            <button className="btn outline" onClick={logout}>Logout</button>
          </div>
        ) : (
          <NavLink to="/login" className={({isActive})=>isActive?'active':''}>Login</NavLink>
        )}
      </div>
    </div>
  )
}
