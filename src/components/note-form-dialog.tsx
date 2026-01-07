"use client";

import { useState, useEffect } from "react";
import { Button } from "@/components/ui/button";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter, DialogDescription } from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { Save, Check } from "lucide-react";
import type { Note } from "@/lib/types";
import { cn } from "@/lib/utils";

const noteColors = ["#FFFFFF", "#F28B82", "#FBBC04", "#FFF475", "#CCFF90", "#A7FFEB", "#CBF0F8", "#AECBFA", "#D7AEFB"];

type NoteFormDialogProps = {
  isOpen: boolean;
  onOpenChange: (isOpen: boolean) => void;
  onSave: (title: string, content: string, color?: string) => void;
  note: Note | null;
  onClose: () => void;
};

export function NoteFormDialog({ isOpen, onOpenChange, onSave, note, onClose }: NoteFormDialogProps) {
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [color, setColor] = useState<string>("#FFFFFF");

  useEffect(() => {
    if (isOpen) {
      if (note) {
        setTitle(note.title);
        setContent(note.content);
        setColor(note.color || "#FFFFFF");
      } else {
        setTitle("");
        setContent("");
        setColor("#FFFFFF");
      }
    }
  }, [note, isOpen]);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (title.trim()) {
      onSave(title, content, color);
    }
  };
  
  const handleOpenChange = (open: boolean) => {
    if (!open) {
      onClose();
    }
    onOpenChange(open);
  }

  return (
    <Dialog open={isOpen} onOpenChange={handleOpenChange}>
      <DialogContent className="sm:max-w-lg">
        <form onSubmit={handleSubmit}>
          <DialogHeader>
            <DialogTitle className="font-headline text-2xl">{note ? "Edit Note" : "Create New Note"}</DialogTitle>
            <DialogDescription>
              {note ? "Modify your note's title and content." : "Add a new note to your collection."}
            </DialogDescription>
          </DialogHeader>
          <div className="grid gap-4 py-6">
            <div className="grid gap-2">
              <label htmlFor="title" className="text-sm font-medium">Title</label>
              <Input
                id="title"
                placeholder="My awesome idea"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                className="text-lg font-semibold"
                required
              />
            </div>
            <div className="grid gap-2">
              <label htmlFor="content" className="text-sm font-medium">Content</label>
              <Textarea
                id="content"
                placeholder="Start writing your note here..."
                value={content}
                onChange={(e) => setContent(e.target.value)}
                className="min-h-[250px] text-base resize-y"
              />
            </div>
            <div className="grid gap-2">
              <label className="text-sm font-medium">Color</label>
              <div className="flex flex-wrap gap-2">
                {noteColors.map((noteColor) => (
                  <button
                    type="button"
                    key={noteColor}
                    className={cn(
                      "w-8 h-8 rounded-full border border-gray-300 flex items-center justify-center",
                      { "ring-2 ring-offset-2 ring-primary": color === noteColor }
                    )}
                    style={{ backgroundColor: noteColor }}
                    onClick={() => setColor(noteColor)}
                  >
                    {color === noteColor && <Check className="w-5 h-5 text-black" />}
                  </button>
                ))}
              </div>
            </div>
          </div>
          <DialogFooter>
            <Button type="submit" size="lg">
              <Save className="mr-2 h-4 w-4" /> Save Note
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
}

