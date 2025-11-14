import { useEffect, useState } from "react";
import { motion } from "framer-motion";
import axios from "axios";
import { ArrowLeft, Download } from "lucide-react";
import { useNavigate } from "react-router-dom";
import { Line } from "react-chartjs-2";
import {
  Chart as ChartJS,
  LineElement,
  CategoryScale,
  LinearScale,
  PointElement,
  Tooltip,
  Filler,
  Legend,
} from "chart.js";

ChartJS.register(LineElement, CategoryScale, LinearScale, PointElement, Tooltip, Filler, Legend);

export default function PortfolioPage() {
  const navigate = useNavigate();
  const [portfolio, setPortfolio] = useState(null);
  const [holdings, setHoldings] = useState([]);
  const [history, setHistory] = useState([]);
  const [loading, setLoading] = useState(true);

  // Fetch portfolio + history
  useEffect(() => {
    const fetchPortfolioData = async () => {
      try {
        const token = localStorage.getItem("jwt");

        // ✅ Fetch portfolio summary
        const res = await axios.get("https://fullstack-project-backend-production.up.railway.app/api/portfolio", {
          headers: { Authorization: `Bearer ${token}` },
        });

        const rawHoldings = res.data.holdings || [];
        const normalized = rawHoldings.map((h) => ({
          symbol: h.stock?.symbol || h.symbol || "N/A",
          name: h.stock?.name || h.name || "Unknown",
          quantity: h.quantity || 0,
          currentPrice: h.currentPrice || 0,
          avgBuyPrice: h.avgBuyPrice || 0,
          currentValue: h.marketValue || h.currentValue || 0,
          unrealizedProfitPercent: h.unrealizedProfitPercent || 0,
          unrealizedProfitValue: h.unrealizedProfitValue || 0,
        }));

        setPortfolio(res.data);
        setHoldings(normalized);

        // ✅ Fetch portfolio growth history
        const historyRes = await axios.get("https://fullstack-project-backend-production.up.railway.app/api/portfolio/history", {
          headers: { Authorization: `Bearer ${token}` },
        });
        setHistory(historyRes.data);
      } catch (err) {
        console.error("Error fetching portfolio data:", err);
      } finally {
        setLoading(false);
      }
    };

    fetchPortfolioData();
  }, []);

  if (loading)
    return (
      <div className="min-h-screen flex justify-center items-center text-xl text-gray-400">
        Loading portfolio...
      </div>
    );

  const totalValue = Number(portfolio?.totalPortfolioValue || 0);
  const cashBalance = Number(portfolio?.cashBalance || 0);
  const stockValue = Number(portfolio?.totalStockValue || 0);

  // ✅ Use backend portfolio history data for chart
  const chartData = {
    labels: history.map((h) => h.date),
    datasets: [
      {
        label: "Portfolio Value",
        data: history.map((h) => h.totalValue),
        borderColor: "#818cf8",
        backgroundColor: "rgba(129,140,248,0.2)",
        tension: 0.4,
        fill: true,
      },
    ],
  };

  const chartOptions = {
    responsive: true,
    plugins: {
      legend: { display: false },
      tooltip: { mode: "index", intersect: false },
    },
    scales: {
      x: {
        grid: { display: false },
      },
      y: {
        ticks: {
          callback: (value) => `$${value.toLocaleString()}`,
        },
      },
    },
  };

  const handleExport = () => {
    window.open("https://fullstack-project-backend-production.up.railway.app/api/trades/export", "_blank");
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-900 via-gray-800 to-gray-900 text-white p-8">
      {/* Header */}
      <div className="flex justify-between items-center mb-6">
        <div className="flex items-center gap-3">
          <button
            onClick={() => navigate("/home")}
            className="p-2 rounded-full bg-gray-800 hover:bg-gray-700 transition-all"
          >
            <ArrowLeft size={18} />
          </button>
          <h1 className="text-2xl font-bold text-indigo-400">My Portfolio</h1>
        </div>

        {/* Export Button */}
        <button
          onClick={handleExport}
          className="flex items-center gap-2 px-4 py-2 bg-indigo-600 hover:bg-indigo-700 rounded-lg text-white transition-all"
        >
          <Download size={18} />
          Export CSV
        </button>
      </div>

      {/* Portfolio Summary */}
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.6 }}
        className="rounded-2xl p-6 mb-8 bg-gray-800/40 border border-gray-700"
      >
        <div className="grid md:grid-cols-3 gap-6 text-center">
          <div>
            <p className="text-gray-400">💰 Cash Balance</p>
            <p className="text-2xl font-semibold">${cashBalance.toLocaleString()}</p>
          </div>
          <div>
            <p className="text-gray-400">📊 Stock Value</p>
            <p className="text-2xl font-semibold">${stockValue.toLocaleString()}</p>
          </div>
          <div>
            <p className="text-gray-400">💼 Total Portfolio</p>
            <p className="text-2xl font-semibold text-indigo-400">
              ${totalValue.toLocaleString()}
            </p>
          </div>
        </div>
      </motion.div>

      {/* Portfolio Growth Chart */}
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.6 }}
        className="rounded-2xl p-6 mb-8 bg-gray-800/40 border border-gray-700"
      >
        <h2 className="text-lg font-semibold mb-3">📈 Portfolio Growth</h2>
        <Line data={chartData} options={chartOptions} />
      </motion.div>

      {/* Holdings List */}
      <h2 className="text-xl font-semibold mb-4">🧾 Current Holdings</h2>
      {holdings.length === 0 ? (
        <p className="text-gray-400 text-center mt-10">
          You currently don’t hold any stocks. Buy some to start building your portfolio!
        </p>
      ) : (
        <div className="grid gap-4">
          {holdings.map((h, i) => (
            <motion.div
              key={i}
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.4, delay: i * 0.05 }}
              className="p-4 rounded-xl bg-gray-800/40 border border-gray-700 flex justify-between items-center"
            >
              <div>
                <h2 className="text-lg font-semibold">{h.symbol}</h2>
                <p className="text-gray-400 text-sm">{h.name}</p>
                <p className="text-gray-400 text-sm">
                  Quantity: {h.quantity}
                </p>
                <p className="text-gray-400 text-sm">
                  Avg Buy: ${h.avgBuyPrice?.toFixed(2)} | Current: ${h.currentPrice?.toFixed(2)}
                </p>
              </div>
              <div className="text-right">
                <p className="text-lg font-semibold">${(h.currentValue || 0).toFixed(2)}</p>
                <p
                  className={`text-sm ${
                    h.unrealizedProfitPercent >= 0 ? "text-green-400" : "text-red-400"
                  }`}
                >
                  {h.unrealizedProfitPercent >= 0 ? "▲" : "▼"}{" "}
                  {(h.unrealizedProfitPercent || 0).toFixed(2)}% (
                  ${(h.unrealizedProfitValue || 0).toFixed(2)})
                </p>
              </div>
            </motion.div>
          ))}
        </div>
      )}
    </div>
  );
}
