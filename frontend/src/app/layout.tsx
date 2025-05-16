import AuthButton from "@/components/AuthButton";
import { AppAuthProvider } from "@/providers/AppAuthProvider";
import { AppContextProvider } from "@/providers/AppContextProvider";
import "@/styles/global.css";
import { Inter } from "next/font/google";
import Link from "next/link";
import type React from "react"; // Import react.

const inter = Inter({ subsets: ["latin"] });

export const metadata = {
  title: "GrubGrade",
  description: "Discover and review local restaurants",
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <AppAuthProvider>
      <AppContextProvider>
        <html lang="en">
          <body className="inter.className">
            <header className="border-b">
              <div className="max-w-[1200px] mx-auto px-4 py-4 flex justify-between items-center">
                <Link href="/" className="text-2xl font-bold">
                  GrubGrades
                </Link>
                <AuthButton />
              </div>
            </header>
            {children}
          </body>
        </html>
      </AppContextProvider>
    </AppAuthProvider>
  );
}
