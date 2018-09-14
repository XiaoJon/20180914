package com.hjhq.teamface.basis.util.dialog;

public enum SheetItemColor {
        Black("#212121"), Blue("#3689E9"), Red("#FC591F");

        private String name;

        private SheetItemColor(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }