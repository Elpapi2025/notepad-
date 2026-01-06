"use client";

import { format } from 'date-fns';
import { Edit, Trash2 } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from '@/components/ui/card';
import type { Note } from '@/lib/types';

type NoteCardProps = {
  note: Note;
  onEdit: (note: Note) => void;
  onDelete: (id: string) => void;
};

export function NoteCard({ note, onEdit, onDelete }: NoteCardProps) {
  return (
    <Card className="flex flex-col transition-shadow duration-300 hover:shadow-xl motion-safe:animate-fade-in-up">
      <CardHeader>
        <CardTitle className="truncate font-headline">{note.title}</CardTitle>
        <CardDescription>
          {format(new Date(note.createdAt), 'MMM d, yyyy h:mm a')}
        </CardDescription>
      </CardHeader>
      <CardContent className="flex-grow">
        <p className="text-sm text-muted-foreground line-clamp-4 whitespace-pre-wrap">{note.content}</p>
      </CardContent>
      <CardFooter className="flex justify-end gap-2 border-t pt-4 mt-auto">
        <Button variant="outline" size="sm" onClick={() => onEdit(note)} aria-label={`Edit note titled ${note.title}`}>
          <Edit className="h-4 w-4 mr-2" />
          Edit
        </Button>
        <Button variant="destructive" size="sm" onClick={() => onDelete(note.id)} aria-label={`Delete note titled ${note.title}`}>
          <Trash2 className="h-4 w-4 mr-2" />
          Delete
        </Button>
      </CardFooter>
    </Card>
  );
}
