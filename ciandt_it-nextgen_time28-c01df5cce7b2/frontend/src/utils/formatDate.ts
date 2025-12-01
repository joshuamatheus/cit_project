export const formatDate = (dateTime: any): string => {
    if (!dateTime) return '';
    
    try {
        // Check if it's already a formatted string
        if (typeof dateTime === 'string') {
            if (dateTime.includes('/')) return dateTime;
            
            // Handle ISO format or simple YYYY-MM-DD
            const datePart = dateTime.split('T')[0]; // Extract date part if ISO format
            const [year, month, day] = datePart.split('-');
            return `${day}/${month}/${year}`;
        }
        
        // Handle Java LocalDateTime object
        // Assuming the LocalDateTime object has methods like getYear(), getMonthValue(), and getDayOfMonth()
        const year = dateTime.year || dateTime.getYear();
        const month = (dateTime.monthValue || dateTime.getMonthValue() || dateTime.getMonth() + 1).toString().padStart(2, '0');
        const day = (dateTime.dayOfMonth || dateTime.getDayOfMonth() || dateTime.getDate()).toString().padStart(2, '0');
        
        return `${day}/${month}/${year}`;
    } catch (error) {
        console.error('Error formatting date:', error);
        return dateTime?.toString() || '';
    }
};
