package Manager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import Components.ComponentClass;
import Entities.EntityClass;
import Main.Vector;

public class MapManager {

    public static int sectionSize = LoadingManager.sectionSize;

    public static Vector topLeftCellPosition = new Vector(0, 0);
    public static ArrayList<Section> loadedSections = new ArrayList<>();

    public static class Cell {
        public Vector absPos = new Vector();
        Vector positionInSection = new Vector();
        public EntityClass entity = null;
        public ComponentClass component = null;

        public Cell(Vector positionInSection, Vector sectionPosition) {
            this.absPos.SetVector(
                    Vector.Add(positionInSection, Vector.Multiply(sectionPosition, sectionSize)));
            this.positionInSection.SetVector(positionInSection);
        }

        public boolean isEmpty() {
            return entity == null && component == null;
        }

        public String getName() {
            if (entity != null) {
                return entity.name;
            } else if (component != null) {
                return component.name;
            } else {
                return null;
            }
        }
    }

    public static class Section {
        Vector position = new Vector();

        Cell[][] cells;

        public Section(Vector position) {
            this.position.SetVector(position);
            this.cells = new Cell[sectionSize][sectionSize];

            for (int i = 0; i < cells.length; i++) {
                for (int j = 0; j < cells[i].length; j++) {
                    this.cells[i][j] = new Cell(new Vector(i, j), this.position);
                }
            }
        }

        public Cell GetCell(Vector posInSection) {
            return cells[posInSection.x][posInSection.y];
        }

        public boolean isEmpty() {
            for (int i = 0; i < cells.length; i++) {
                for (int j = 0; j < cells[i].length; j++) {
                    if (!cells[i][j].isEmpty()) {
                        return false;
                    }
                }
            }
            return true;
        }

        public boolean isInScreen() {
            if (this.position.x <= topLeftCellPosition.x + LoadingManager.maxScreenCol &&
                    this.position.x + sectionSize >= topLeftCellPosition.x &&
                    this.position.y <= topLeftCellPosition.y + LoadingManager.maxScreenRow &&
                    this.position.y + sectionSize >= topLeftCellPosition.y) {
                return true;
            }
            return false;
        }

        public void PrintOutSectionInfo() {
            System.out.print("**");
            this.position.Print();
            System.out.println();
            for (int i = 0; i < this.cells.length; i++) {
                for (int j = 0; j < this.cells[i].length; j++) {
                    if (!this.cells[i][j].isEmpty()) {
                        this.cells[i][j].positionInSection.Print();
                        System.out.println(this.cells[i][j].getName());
                    }
                }
            }
        }

        public ArrayList<Cell> GetOccupiedCells() {
            ArrayList<Cell> retCells = new ArrayList<>();
            for (int i = 0; i < this.cells.length; i++) {
                for (int j = 0; j < this.cells[i].length; j++) {
                    if (!this.cells[i][j].isEmpty()) {
                        retCells.add(this.cells[i][j]);
                    }
                }
            }
            return retCells;
        }
    }

    public static Cell GetCellFromAbsPos(Vector absPos, boolean loadWhenNotFound) {
        Vector sectionPos = AbsPosToSectionPos(absPos);

        Section section = GetSectionFromSectionPos(sectionPos);

        if (section == null) {
            if (loadWhenNotFound) {
                section = LoadSection(sectionPos);
            } else {
                return null;
            }
        }

        Vector posInSection = new Vector(absPos.Minus(section.position.Multiply(sectionSize)));
        Cell retCell = section.GetCell(posInSection);

        if (!retCell.absPos.equals(absPos)) {
            System.err.println("GetCellFromAbsPos failed to get cell.");
        }

        return section.GetCell(posInSection);
    }

    public static Section GetSectionFromAbsPos(Vector absPos) {

        Vector sectionPos = AbsPosToSectionPos(absPos);

        for (Section section : loadedSections) {
            if (section != null) {
                if (section.position.equals(sectionPos)) {
                    return section;
                }
            }
        }

        return null;
    }

    public static Section GetSectionFromSectionPos(Vector sectionPos) {
        for (Section section : loadedSections) {
            if (section != null) {
                if (section.position.equals(sectionPos)) {
                    return section;
                }
            }
        }

        return null;
    }

    public static Cell[][] GetArrayOfCells(Vector beginPos, Vector size) {
        Cell[][] returnArray = new Cell[size.x][size.y];

        for (int i = 0; i < size.x; i++) {
            for (int j = 0; j < size.y; j++) {
                returnArray[i][j] = GetCellFromAbsPos(beginPos.Add(new Vector(i, j)), true);
            }
        }

        return returnArray;
    }

    public static Section LoadSection(Vector sectionPos) {
        for (Section section : loadedSections) {
            if (sectionPos.equals(section.position)) {
                return section;
            }
        }

        Section section = new Section(sectionPos);
        try {
            section = SavingManager.LoadSectionFromFile(sectionPos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (section == null) {
            section = new Section(sectionPos);
            AddLoadedSection(section);
            return section;
        }
        AddLoadedSection(section);
        return section;
    }

    public static void MoveMap(Vector lastCursorPos, Vector cursorEndPos) {
        Vector moveVector = lastCursorPos.Minus(cursorEndPos);
        topLeftCellPosition.AddEqual(moveVector);
        ArrayList<Section> sectionsToUnload = new ArrayList<>();
        ArrayList<Vector> posToCheckForToLoad = new ArrayList<>();

        if (moveVector.x < 0) {// Check for left
            for (int i = 0; i < LoadingManager.maxScreenRow; i += sectionSize) {
                posToCheckForToLoad.add(topLeftCellPosition.Add(new Vector(0, i)));
            }
        } else if (moveVector.x > 0) {// Check for right
            for (int i = 0; i < LoadingManager.maxScreenRow; i += sectionSize) {
                posToCheckForToLoad.add(topLeftCellPosition.Add(new Vector(LoadingManager.maxScreenCol - 1, i)));
            }
        }
        if (moveVector.y < 0) {// Check for top
            for (int i = 0; i < LoadingManager.maxScreenCol; i += sectionSize) {
                posToCheckForToLoad.add(topLeftCellPosition.Add(new Vector(i, 0)));
            }
        } else if (moveVector.y > 0) {// Check for bottom
            for (int i = 0; i < LoadingManager.maxScreenCol; i += sectionSize) {
                posToCheckForToLoad.add(topLeftCellPosition.Add(new Vector(LoadingManager.maxScreenRow - 1, i)));
            }
        }

        for (Vector position : posToCheckForToLoad) {
            Vector sectionPos = AbsPosToSectionPos(position);
            try {
                if (!SavingManager.CheckIfSectionInFileIsEmpty(sectionPos)) {

                    LoadSection(sectionPos);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        for (Section section : loadedSections) {
            if (!section.isInScreen()) {
                try {
                    SavingManager.SaveSectionToFile(section);
                    sectionsToUnload.add(section);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        for (Section sectionToUnload : sectionsToUnload) {
            loadedSections.remove(sectionToUnload);
        }
    }

    public static boolean CheckIfInScreen(Vector absPos) {
        Vector toScreenPos = absPos.Minus(topLeftCellPosition);
        if (toScreenPos.x <= LoadingManager.maxScreenCol - 1 && toScreenPos.x >= 0
                && toScreenPos.y <= LoadingManager.maxScreenRow && toScreenPos.y >= 0) {
            return true;
        }
        return false;
    }

    public static void AddLoadedSection(Section section) {
        if (section != null) {
            loadedSections.add(section);
        }
    }

    public static Vector AbsPosToSectionPos(Vector absPos) {
        int fullAbsPosX;
        int fullAbsPosY;

        if (absPos.x < 0 && absPos.x % sectionSize != 0) {
            fullAbsPosX = absPos.x - (absPos.x % sectionSize + sectionSize);
        } else {
            fullAbsPosX = absPos.x;
        }
        if (absPos.y < 0 && absPos.y % sectionSize != 0) {
            fullAbsPosY = absPos.y - (absPos.y % sectionSize + sectionSize);
        } else {
            fullAbsPosY = absPos.y;
        }

        Vector sectionPos = new Vector(fullAbsPosX / sectionSize, fullAbsPosY / sectionSize);

        return sectionPos;
    }

}