import React from "react";
import { createRoot } from "react-dom/client";
import { HashRouter as Router } from "react-router-dom"; // ← 用 HashRouter 更稳
import App from "./App";
import "./styles.css";

createRoot(document.getElementById("root")).render(
  <Router>
    <App />
  </Router>
);
