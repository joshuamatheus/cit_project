import Header from "@/components/header/header";

export default function PrivateLayout({ children }: { children: React.ReactNode }) {
    return (
        <div>
            {/* <h1>Public route:</h1> */}
            <div>{children}</div>
        </div>
    );
}
