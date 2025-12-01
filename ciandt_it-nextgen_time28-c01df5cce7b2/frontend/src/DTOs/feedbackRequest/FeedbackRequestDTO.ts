interface FeedbackRequestDTO {
    id: string;
    questions?: string[];
    appraisers: string[];
    appraiserStatus: Record<string, string>;
    requesterId: string;
    createdAt: string;
}