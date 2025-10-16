import { useState } from "react";

function App() {
  const [drawings, setDrawings] = useState([]);
  const [color, setColor] = useState("#0000ff"); // default blue

  // Handle drawing on SVG click
  const handleClick = (e) => {
    // Get mouse coordinates relative to SVG
    const svgRect = e.currentTarget.getBoundingClientRect();
    const cx = e.clientX - svgRect.left;
    const cy = e.clientY - svgRect.top;

    const newCircle = { cx, cy, r: 8, fill: color };
    setDrawings((prev) => [...prev, newCircle]);
  };

  // Undo last circle
  const undo = () => {
    setDrawings((prev) => prev.slice(0, -1));
  };

  return (
    <div style={{ padding: "20px" }}>
      <h2>Interactive SVG Drawing Tool</h2>

      {/* SVG Canvas */}
      <svg
        id="canvas"
        width={500}
        height={400}
        style={{ border: "1px solid black" }}
        onClick={handleClick}
      >
        {drawings.map((circle, index) => (
          <circle
            key={index}
            cx={circle.cx}
            cy={circle.cy}
            r={circle.r}
            fill={circle.fill}
          />
        ))}
      </svg>

      {/* Controls */}
      <div style={{ marginTop: "10px" }}>
        <label>
          Select Color:{" "}
          <input
            type="color"
            value={color}
            onChange={(e) => setColor(e.target.value)}
          />
        </label>
        <button onClick={undo} style={{ marginLeft: "10px" }}>
          Undo
        </button>
      </div>
    </div>
  );
}

export default App;