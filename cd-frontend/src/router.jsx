import { createBrowserRouter } from "react-router-dom";
import { Screen } from "./components/Screen";

export const router = createBrowserRouter([
    {
        path: "/",
        element: <Screen />
    }
])