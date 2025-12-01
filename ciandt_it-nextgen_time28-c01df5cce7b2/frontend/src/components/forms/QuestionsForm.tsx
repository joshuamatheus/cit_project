'use client'
import React, { useState, useEffect } from 'react';
import { TextField, List, ListItem, ListItemText, Box } from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import DeleteIcon from '@mui/icons-material/Delete';
import { ActionButton, ActionIconButton } from '../buttons/ActionButton';
import MenuBox from '../boxes/MenuBox';
import theme from '@/theme';

interface Question {
  id: number;
  text: string;
}

interface QuestionsFormProps {
  initialQuestions?: Question[];
  onQuestionsChange?: (questions: Question[]) => void;
}

const MAX_QUESTIONS = 3;
const STORAGE_KEY = 'user_custom_questions';

const QuestionsForm: React.FC<QuestionsFormProps> = ({ 
  initialQuestions,
  onQuestionsChange 
}) => {
  const [inputQuestion, setInputQuestion] = useState<string>('');
  const [questions, setQuestions] = useState<Question[]>([]);
  const [idCounter, setIdCounter] = useState<number>(1);
  const [isInitialized, setIsInitialized] = useState<boolean>(false);

  // Load saved questions from localStorage or use initialQuestions if provided
  useEffect(() => {
    if (isInitialized) return;
    
    if (initialQuestions && initialQuestions.length > 0) {
      setQuestions(initialQuestions);

      const maxId = initialQuestions.reduce(
        (max: number, question: Question) => Math.max(max, question.id), 
        0
      );
      setIdCounter(maxId + 1);
    } else {
      try {
        const savedQuestionsData = localStorage.getItem(STORAGE_KEY);
        
        if (savedQuestionsData) {
          const savedData = JSON.parse(savedQuestionsData);
          setQuestions(savedData.questions || []);
          
          const maxId = savedData.questions.reduce(
            (max: number, question: Question) => Math.max(max, question.id), 
            0
          );
          setIdCounter(maxId + 1);
        }
      } catch (error) {
        console.error('Erro ao carregar as perguntas salvas:', error);
      }
    }
    
    setIsInitialized(true);
  }, [initialQuestions, isInitialized]);

  // Save questions to localStorage and notify parent via callback if provided
  useEffect(() => {
    if (!isInitialized) return;
    
    // Callback to parent if provided
    if (onQuestionsChange) {
      onQuestionsChange(questions);
    }
    
    // Save to localStorage if no external state management is used
    if (!onQuestionsChange) {
      try {
        const dataToSave = {
          questions,
          lastUpdated: new Date().toISOString()
        };
        localStorage.setItem(STORAGE_KEY, JSON.stringify(dataToSave));
      } catch (error) {
        console.error('Erro ao salvar as perguntas:', error);
      }
    }
  }, [questions, onQuestionsChange, isInitialized]);

  const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setInputQuestion(event.target.value);
  };

  const handleAddQuestion = () => {
    if (inputQuestion.trim() && questions.length < MAX_QUESTIONS) {
      const newQuestions = [...questions, { id: idCounter, text: inputQuestion }];
      setQuestions(newQuestions);
      setIdCounter(idCounter + 1);
      setInputQuestion('');
    }
  };

  const handleKeyDown = (event: React.KeyboardEvent<HTMLInputElement>) => {
    if (event.key === 'Enter') {
      handleAddQuestion();
    }
  };

  const handleDeleteQuestion = (id: number) => {
    const updatedQuestions = questions.filter(question => question.id !== id);
    setQuestions(updatedQuestions);
  };

  const isMaxQuestionReached = questions.length >= MAX_QUESTIONS;

  return (
    <MenuBox border={`1px solid ${theme.palette.divider}`}>
      <Box sx={{ 
        display: 'flex', 
        width: '100%',
        mb: 2
      }}>
        <TextField
          fullWidth
          placeholder="Inserir pergunta"
          variant="outlined"
          value={inputQuestion}
          onChange={handleInputChange}
          onKeyDown={handleKeyDown}
          sx={{ 
            mr: 1,
            '& .MuiOutlinedInput-root': {
              borderRadius: '4px',
              backgroundColor: theme.palette.background.paper,
            }
          }}
          disabled={isMaxQuestionReached}
          size="small"
        />
        <ActionButton 
          icon={<AddIcon />} 
          backgroundColor='blue' 
          onClick={handleAddQuestion}
          disabled={!inputQuestion.trim() || isMaxQuestionReached}
        />
      </Box>

      <List sx={{ 
        p: 0,
        width: '100%' 
      }}>
        {questions.map((question, index) => (
          <ListItem
            key={question.id}
            disableGutters
            secondaryAction={
              <ActionIconButton
                icon={<DeleteIcon sx={{ color: theme.palette.buttons.red }} fontSize="small" />}
                onClick={() => handleDeleteQuestion(question.id)}
              />
            }
            sx={{ 
              borderBottom: '1px solid #f0f0f0',
              py: 1.5,
              px: 0
            }}
          >
            <ListItemText
              primary={`${index + 1}. ${question.text}`}
              sx={{
                '& .MuiListItemText-primary': {
                  pr: 4,
                  wordWrap: 'break-word',
                  wordBreak: 'break-word',
                  overflow: 'hidden',
                  lineHeight: '25px'
                }
              }}
            />
          </ListItem>
        ))}
      </List>
    </MenuBox>
  );
};

export default QuestionsForm;