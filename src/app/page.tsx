"use client";

import { useState, useEffect } from "react";
import { Plus } from "lucide-react";
import { Button } from "@/components/ui/button";
import { useToast } from "@/hooks/use-toast";
import { NoteCard } from "@/components/note-card";
import { NoteFormDialog } from "@/components/note-form-dialog";
import type { Note } from "@/lib/types";
import { Skeleton } from "@/components/ui/skeleton";
import { Card, CardContent, CardFooter, CardHeader } from "@/components/ui/card";

export default function Home() {
  const [notes, setNotes] = useState<Note[]>([]);
  const [selectedNote, setSelectedNote] = useState<Note | null>(null);
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [isClient, setIsClient] = useState(false);
  const { toast } = useToast();

  useEffect(() => {
    setIsClient(true);
    try {
      const savedNotes = localStorage.getItem("green-notes");
      if (savedNotes) {
        setNotes(JSON.parse(savedNotes));
      }
    } catch (error) {
      console.error("Failed to load notes from localStorage", error);
      toast({
        variant: "destructive",
        title: "Error",
        description: "Could not load your notes.",
      });
    }
  }, [toast]);

  useEffect(() => {
    if (isClient) {
      try {
        localStorage.setItem("green-notes", JSON.stringify(notes));
      } catch (error) {
        console.error("Failed to save notes to localStorage", error);
        toast({
          variant: "destructive",
          title: "Error",
          description: "Could not save your notes.",
        });
      }
    }
  }, [notes, isClient, toast]);

  const handleNewNoteClick = () => {
    setSelectedNote(null);
    setIsDialogOpen(true);
  };

  const handleEditNoteClick = (note: Note) => {
    setSelectedNote(note);
    setIsDialogOpen(true);
  };

  const handleDeleteNote = (id: string) => {
    setNotes(notes.filter((note) => note.id !== id));
    toast({
      title: "Note deleted",
      description: "Your note has been successfully deleted.",
    });
  };

  const handleSaveNote = (title: string, content: string, color?: string) => {
    if (selectedNote) {
      setNotes(
        notes.map((note) =>
          note.id === selectedNote.id ? { ...note, title, content, color } : note
        )
      );
      toast({
        title: "Note updated",
        description: "Your note has been successfully updated.",
      });
    } else {
      const newNote: Note = {
        id: new Date().toISOString(),
        title,
        content,
        createdAt: new Date().toISOString(),
        color,
      };
      setNotes([newNote, ...notes]);
      toast({
        title: "Note created",
        description: "Your new note has been successfully created.",
      });
    }
    setIsDialogOpen(false);
    setSelectedNote(null);
  };
  
  if (!isClient) {
    return (
      <div className="min-h-screen bg-background">
        <header className="py-6 px-4 md:px-8 border-b border-border sticky top-0 bg-background/80 backdrop-blur-sm z-10">
          <div className="container mx-auto flex justify-between items-center">
            <h1 className="text-3xl font-bold font-headline text-primary">GreenNote</h1>
            <Button className="shadow-sm" disabled>
              <Plus className="mr-2 h-4 w-4" /> New Note
            </Button>
          </div>
        </header>
        <main className="container mx-auto p-4 md:p-8">
          <div className="grid gap-6 sm:grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
            {[...Array(4)].map((_, i) => (
              <Card key={i}>
                <CardHeader>
                  <Skeleton className="h-6 w-3/4" />
                  <Skeleton className="h-4 w-1/2 mt-2" />
                </CardHeader>
                <CardContent>
                  <Skeleton className="h-4 w-full mb-2" />
                  <Skeleton className="h-4 w-full mb-2" />
                  <Skeleton className="h-4 w-5/6" />
                </CardContent>
                <CardFooter className="flex justify-end gap-2 border-t pt-4 mt-auto">
                  <Skeleton className="h-9 w-20" />
                  <Skeleton className="h-9 w-24" />
                </CardFooter>
              </Card>
            ))}
          </div>
        </main>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-background flex flex-col">
      <header className="py-6 px-4 md:px-8 border-b border-border sticky top-0 bg-background/95 backdrop-blur-sm z-10">
        <div className="container mx-auto flex justify-between items-center">
          <h1 className="text-3xl font-bold font-headline text-primary">GreenNote</h1>
          <Button onClick={handleNewNoteClick} className="shadow-sm">
            <Plus className="mr-2 h-4 w-4" /> New Note
          </Button>
        </div>
      </header>

      <main className="container mx-auto p-4 md:p-8 flex-grow">
        {notes.length === 0 ? (
          <div className="flex items-center justify-center h-full text-center py-20">
            <div>
              <svg className="mx-auto h-24 w-24 text-muted-foreground/50" fill="none" viewBox="0 0 24 24" stroke="currentColor" aria-hidden="true">
                <path vectorEffect="non-scaling-stroke" strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
              </svg>
              <h2 className="mt-6 text-2xl font-semibold text-foreground font-headline">No notes yet</h2>
              <p className="mt-2 text-muted-foreground">Get started by creating a new note.</p>
              <Button onClick={handleNewNoteClick} className="mt-6">
                <Plus className="mr-2 h-4 w-4" /> Create Note
              </Button>
            </div>
          </div>
        ) : (
          <div className="grid gap-6 sm:grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
            {notes.map((note) => (
              <NoteCard
                key={note.id}
                note={note}
                onEdit={handleEditNoteClick}
                onDelete={handleDeleteNote}
              />
            ))}
          </div>
        )}
      </main>

      <NoteFormDialog
        isOpen={isDialogOpen}
        onOpenChange={setIsDialogOpen}
        onSave={handleSaveNote}
        note={selectedNote}
        onClose={() => setSelectedNote(null)}
      />
    </div>
  );
}
