import React, { createContext, useContext, useEffect, useState } from 'react'
import { api, setToken, getToken } from './api'

const AuthCtx = createContext(null)
export function useAuth(){ return useContext(AuthCtx) }

export function AuthProvider({ children }){
  const [user, setUser] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(()=>{
    const t = getToken()
    if(!t){ setLoading(false); return }
    api.me().then(setUser).catch(()=>setToken('')).finally(()=>setLoading(false))
  }, [])

  const login = async (username, password) => {
    const r = await api.login(username, password)
    setToken(r.accessToken)
    const me = await api.me()
    setUser(me)
  }
  const register = async (username, email, password) => {
    await api.register(username, email, password)
    await login(username, password)
  }
  const logout = () => { setToken(''); setUser(null) }

  return <AuthCtx.Provider value={{ user, setUser, loading, login, register, logout }}>{children}</AuthCtx.Provider>
}
