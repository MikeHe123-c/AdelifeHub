import React, { useEffect, useState } from "react";
import { Link, useSearchParams, useNavigate } from "react-router-dom";
import { api } from "../api";

const TABS = [
    { key: "rental", label: "租房" },
    { key: "job", label: "工作" },
    { key: "post", label: "帖子" },
];

export default function FavoritesPage() {
    const [sp, setSp] = useSearchParams();
    const tab = sp.get("type") || "rental";
    const [list, setList] = useState([]);
    const [meta, setMeta] = useState({ page: 1, size: 10, total: 0 });
    const nav = useNavigate();

    useEffect(() => {
        let page = Number(sp.get("page") || 1);
        api.meFavorites({ type: tab, page, size: 10 })
            .then((res) => {
                setList(res.data || []);
                setMeta(res.meta || { page, size: 10, total: 0 });
            })
            .catch((e) => {
                if (String(e.message).includes("UNAUTHORIZED")) {
                    nav("/login?next=/favorites");
                }
            });
    }, [tab, sp, nav]);

    const switchTab = (k) => {
        setSp({ type: k, page: 1 });
    };

    const unfav = async (item) => {
        if (tab === "post") {
            await api.unfavoritePost(item.id);
        } else {
            await api.unfavoriteListing(item.id);
        }
        api.meFavorites({ type: tab, page: meta.page, size: meta.size })
            .then((res) => {
                setList(res.data || []);
                setMeta(res.meta || meta);
            });
    };

    const toDetailPath = (item) =>
        tab === "post" ? `/posts/${item.id}` : `/listings/${item.id}`;

    return (
        <div className="container">
            <h2>我的收藏</h2>

            <div style={{ display: "flex", gap: 12, margin: "12px 0" }}>
                {TABS.map((t) => (
                    <button
                        key={t.key}
                        onClick={() => switchTab(t.key)}
                        style={{
                            padding: "6px 12px",
                            borderRadius: 8,
                            border: "1px solid #ddd",
                            background: t.key === tab ? "#635bff" : "#fff",
                            color: t.key === tab ? "#fff" : "#333",
                            cursor: "pointer",
                        }}
                    >
                        {t.label}
                    </button>
                ))}
            </div>

            {list.length === 0 ? (
                <div style={{ color: "#888", marginTop: 24 }}>暂无收藏</div>
            ) : (
                <ul style={{ listStyle: "none", padding: 0, marginTop: 8 }}>
                    {list.map((it) => (
                        <li
                            key={it.id}
                            style={{
                                padding: 12,
                                marginBottom: 10,
                                border: "1px solid #eee",
                                borderRadius: 12,
                                display: "flex",
                                alignItems: "center",
                                justifyContent: "space-between",
                            }}
                        >
                            <div style={{ display: "flex", gap: 12, alignItems: "center" }}>
                                <div style={{ width: 56, height: 56, background: "#f5f5f5", borderRadius: 8 }} />
                                <div>
                                    <Link to={toDetailPath(it)} style={{ fontWeight: 600 }}>
                                        {it.title || it.content?.slice(0, 40)}
                                    </Link>
                                    <div style={{ color: "#999", fontSize: 12 }}>
                                        {tab === "post" ? "帖子" : it.type === "job" ? "工作" : "租房"}
                                    </div>
                                </div>
                            </div>
                            <button
                                onClick={() => unfav(it)}
                                style={{
                                    padding: "6px 10px",
                                    borderRadius: 8,
                                    border: "1px solid #e0e0e0",
                                    background: "#fff",
                                    cursor: "pointer",
                                }}
                            >
                                取消收藏
                            </button>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
}
