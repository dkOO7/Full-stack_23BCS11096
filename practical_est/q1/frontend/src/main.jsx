import './index.css'
import { createRoot } from 'react-dom/client'
import Welcome from './Welcome'

const root = createRoot(document.getElementById('app'))

root.render(<Welcome />)
