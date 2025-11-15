import { useState } from 'react'

export default function Welcome() {
  const [name, setName] = useState('')
  const [submitted, setSubmitted] = useState(false)
  const [inputName, setInputName] = useState('')

  const handleSubmit = (e) => {
    e.preventDefault()
    setName(inputName)
    setSubmitted(true)
  }

  const handleSkip = () => {
    setName('')
    setSubmitted(true)
  }

  const handleReset = () => {
    setSubmitted(false)
    setInputName('')
    setName('')
  }

  if (submitted) {
    return (
      <div className="flex items-center justify-center min-h-screen bg-gradient-to-br from-slate-400 to bg-slate-100">
        <div className="bg-white rounded-lg shadow-2xl p-8 text-center max-w-md">
          <h1 className="text-4xl font-bold text-black mb-4">
            Welcome {name ? name : 'Guest'}!
          </h1>
          <p className="text-gray-600 text-lg mb-6">
            {`Hope you have a good day ahead ` }
          </p>
          <button
            onClick={handleReset}
            className="px-6 py-2 bg-indigo-600 text-white rounded-lg font-semibold hover:bg-indigo-700 transition"
          >
            Back
          </button>
        </div>
      </div>
    )
  }

  return (
    <div className="flex items-center justify-center min-h-screen bg-gradient-to-br from-slate-400 to bg-slate-100">
      <div className="bg-white rounded-lg shadow-2xl p-8 w-full max-w-md">
        <h1 className="text-3xl font-bold text-black-600 mb-6 text-center">
          Welcome!
        </h1>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-gray-700 font-semibold mb-2">
              Enter your name:
            </label>
            <input
              type="text"
              value={inputName}
              onChange={(e) => setInputName(e.target.value)}
              placeholder="Your name"
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-black"
            />
          </div>
          <div className="flex gap-3">
            <button
              type="submit"
              className="flex-1 px-4 py-2 bg-black text-white rounded-lg font-semibold hover:bg-gray-500 transition "
            >
              Submit
            </button>
            <button
              type="button"
              onClick={handleSkip}
              className="flex-1 px-4 py-2 bg-gray-400 text-white rounded-lg font-semibold hover:bg-black transition"
            >
              Skip
            </button>
          </div>
        </form>
      </div>
    </div>
  )
}
