export interface IActionButton {
    icon?: React.ReactNode;
    label?: string;
    backgroundColor?: 'blue' | "pink";
    color?: 'black' | 'pink';
    onClick?: () => void;
    disabled?: boolean;
}