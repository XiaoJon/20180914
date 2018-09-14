package com.hjhq.teamface.basis.util.dialog;

public  class SheetItem {
        String name;
        OnSheetItemClickListener itemClickListener;
        SheetItemColor color;

        public SheetItem(String name) {
            this(name, SheetItemColor.Black, null);
        }

        public SheetItem(String name, SheetItemColor color) {
            this(name, color, null);
        }

        public SheetItem(String name,
                         OnSheetItemClickListener itemClickListener) {
            this(name, SheetItemColor.Black, itemClickListener);
        }

        public SheetItem(String name, SheetItemColor color,
                         OnSheetItemClickListener itemClickListener) {
            this.name = name;
            this.color = color;
            this.itemClickListener = itemClickListener;
        }
    }