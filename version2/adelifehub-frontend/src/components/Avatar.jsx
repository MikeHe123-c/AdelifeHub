import React from 'react'
export default function Avatar({ url, name, size=36 }){
  if (url) return <img className="avatar" style={{width:size,height:size}} src={url} alt="avatar"/>
  const c = (name||'?').toUpperCase().slice(0,1)
  return <span className="avatar" style={{width:size,height:size}}>{c}</span>
}
