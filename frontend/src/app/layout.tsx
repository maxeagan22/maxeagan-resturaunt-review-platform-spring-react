import AuthButton from "@/components/AuthButton";
import { AppAuthProvider } from "@/providers/AppAuthProvider";
import { AppContextProvider } from "@/providers/AppContextProvider";
import "@/styles/globals.css";
import { Inter } from "next/font/google";
import Link from "next/link";
import type React from "react"; // Import React for typing

// Load the Inter font from Google Fonts
const inter = Inter({ subsets: ["latin"] });

/**
 * Global site metadata for SEO and browser context
 */
export const metadata = {
  title: "GrubGrade",
  description: "Discover and review local restaurants",
};

/**
 * Root layout for the entire application.
 *
 * Wraps all pages with global providers like authentication and app context.
 * Includes global styles, header navigation, and font styling.
 *
 * @param {Object} props - Props for the layout
 * @param {React.ReactNode} props.children - The content of the current page
 * @returns {JSX.Element} The wrapped app layout
 */
export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <AppAuthProvider>
      <AppContextProvider>
        <html lang="en">
          <body className={inter.className}>
            {/* Site Header */}
            <header className="border-b">
              <div className="max-w-[1200px] mx-auto px-4 py-4 flex justify-between items-center">
                <Link href="/" className="text-2xl font-bold">
                  GrubGrades
                </Link>
                <AuthButton />
              </div>
            </header>

            {/* Page Content */}
            {children}
          </body>
        </html>
      </AppContextProvider>
    </AppAuthProvider>
  );
}
