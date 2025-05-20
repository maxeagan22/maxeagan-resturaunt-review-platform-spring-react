"use client";

import Link from "next/link";
import { Button } from "@/components/ui/button";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { useAuth } from "react-oidc-context";

/**
 * Authentication button component.
 *
 * Shows a "Login" button if the user is not authenticated.
 * If authenticated, displays a user avatar as a dropdown trigger.
 * The dropdown contains:
 *  - Link to "Add Restaurant" page
 *  - Logout option
 *
 * Handles login and logout via OIDC authentication flows.
 *
 * No props required.
 */
export default function AuthButton() {
  const { signinRedirect, signoutRedirect, isAuthenticated } = useAuth();

  /**
   * Initiates login flow via redirect.
   */
  const handleLogin = async () => {
    try {
      await signinRedirect();
    } catch (error) {
      console.error("Login failed: ", error);
    }
  };

  /**
   * Initiates logout flow via redirect.
   */
  const handleLogout = async () => {
    try {
      await signoutRedirect();
    } catch (error) {
      console.error("Logout failed: ", error);
    }
  };

  if (isAuthenticated) {
    return (
      <DropdownMenu>
        <DropdownMenuTrigger>
          <Avatar>
            <AvatarImage
              src="/placeholder.svg?height=32&width=32"
              alt="User avatar"
            />
            <AvatarFallback>U</AvatarFallback>
          </Avatar>
        </DropdownMenuTrigger>
        <DropdownMenuContent align="end">
          <DropdownMenuItem asChild>
            <Link href="/restaurants/create">Add Restaurant</Link>
          </DropdownMenuItem>
          <DropdownMenuItem onClick={handleLogout}>Logout</DropdownMenuItem>
        </DropdownMenuContent>
      </DropdownMenu>
    );
  }

  return <Button onClick={handleLogin}>Login</Button>;
}
