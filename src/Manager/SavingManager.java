package Manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import Components.ComponentClass;
import Components.Starters;
import Entities.EntityClass;
import Main.UserInput;
import Main.Vector;
import Manager.MapManager.Cell;
import Manager.MapManager.Section;

public class SavingManager {

    public static UserInput userInput;

    static String saveFolderDir = LoadingManager.currentDir + "save\\";
    public static String gameRunFileName = LoadingManager.currentDir + "run\\gameRun.txt";

    public static ArrayList<String> allFileNames = new ArrayList<>();
    static String availableNewFileName = "saveFile";

    public static String currentSaveName;
    public static File gameRunFile = new File(gameRunFileName);
    static File currentSave;

    public static void SaveSectionToFile(Section section) throws IOException {

        if (section.isEmpty()) {
            return;
        }

        Vector[] allLoadedSectionPos;
        boolean foundInLoaded = false;

        if (currentSave == null) {
            RefreshSaveFiles();
        }

        ArrayList<String> fileContent = GetAllFromFile(currentSave);

        if (fileContent.size() > 0) {
            allLoadedSectionPos = ExtractAllLoadedSectionPos(fileContent.get(0));

            for (Vector position : allLoadedSectionPos) {
                if (position.equals(section.position)) {
                    foundInLoaded = true;
                    break;
                }
            }
        }

        if (foundInLoaded) {
            fileContent.set(0, fileContent.get(0).replace(section.position.FormatToBrackets(), ""));
            if (!section.isEmpty()) {
                fileContent.set(0, fileContent.get(0) + section.position.FormatToBrackets());
            }

            for (int i = 1; i < fileContent.size(); i++) {
                if (fileContent.get(i).charAt(0) == '*') {
                    if (fileContent.get(i).contains(section.position.FormatToBrackets())) {
                        fileContent.remove(i);
                        for (int j = i; j < fileContent.size(); j++) {
                            if (fileContent.get(i).charAt(0) == '*') {
                                break;
                            }
                            fileContent.remove(j);
                            j--;
                        }
                    }
                }
            }
        } else if (!section.isEmpty()) {

            if (fileContent.isEmpty()) {
                fileContent.add(section.position.FormatToBrackets());
            } else {
                fileContent.set(0, fileContent.get(0) + section.position.FormatToBrackets());
            }
        }

        if (!section.isEmpty()) {

            fileContent.add("*" + section.position.FormatToBrackets());

            for (int i = 0; i < section.cells.length; i++) {
                for (int j = 0; j < section.cells[i].length; j++) {
                    if (!section.cells[i][j].isEmpty()) {
                        fileContent.add(section.cells[i][j].positionInSection.FormatToBrackets()
                                + section.cells[i][j].getName());
                    }
                }
            }
        }

        FileWriter fileWriter = new FileWriter(currentSave);

        for (String line : fileContent) {
            fileWriter.write(line + "\n");
        }

        fileWriter.close();
    }

    public static void SaveEverythingToFile(String fileName) throws IOException {
        if (MapManager.loadedSections.isEmpty()) {
            return;
        }

        Vector[] allLoadedSectionPos = new Vector[0];
        ArrayList<Section> existInFile = new ArrayList<>();
        ArrayList<Section> notExistInFile = new ArrayList<>();

        if (currentSave == null) {
            RefreshSaveFiles();
        }

        ArrayList<String> fileContent = GetAllFromFile(currentSave);

        if (fileContent.size() > 0) {
            allLoadedSectionPos = ExtractAllLoadedSectionPos(fileContent.get(0));
        }

        boolean foundInLoaded = false;
        for (Section section : MapManager.loadedSections) {
            for (Vector position : allLoadedSectionPos) {
                if (position.equals(section.position) && !section.isEmpty()) {
                    existInFile.add(section);
                    foundInLoaded = true;
                    break;
                }
            }
            if (!foundInLoaded) {
                if (!section.isEmpty()) {
                    notExistInFile.add(section);
                }
            }
        }

        for (Section tarSection : existInFile) {

            fileContent.set(0, fileContent.get(0).replace(tarSection.position.FormatToBrackets(), ""));
            fileContent.set(0, fileContent.get(0) + tarSection.position.FormatToBrackets());
            for (int i = 1; i < fileContent.size(); i++) {
                if (fileContent.get(i).charAt(0) == '*') {
                    if (fileContent.get(i).contains(tarSection.position.FormatToBrackets())) {
                        fileContent.remove(i);
                        for (int j = i; j < fileContent.size(); j++) {
                            if (fileContent.get(i).charAt(0) == '*') {
                                break;
                            }
                            fileContent.remove(j);
                            j--;
                        }
                    }
                }
            }

            fileContent.add("*" + tarSection.position.FormatToBrackets());

            for (int i = 0; i < tarSection.cells.length; i++) {
                for (int j = 0; j < tarSection.cells[i].length; j++) {
                    if (!tarSection.cells[i][j].isEmpty()) {
                        fileContent.add(tarSection.cells[i][j].positionInSection.FormatToBrackets()
                                + tarSection.cells[i][j].getName());
                    }
                }
            }
        }

        for (Section tarSection : notExistInFile) {
            if (fileContent.size() == 0) {
                fileContent.add(tarSection.position.FormatToBrackets());
            } else {
                fileContent.set(0, fileContent.get(0) + tarSection.position.FormatToBrackets());
            }

            fileContent.add("*" + tarSection.position.FormatToBrackets());

            for (int i = 0; i < tarSection.cells.length; i++) {
                for (int j = 0; j < tarSection.cells[i].length; j++) {
                    if (!tarSection.cells[i][j].isEmpty()) {
                        fileContent.add(tarSection.cells[i][j].positionInSection.FormatToBrackets()
                                + tarSection.cells[i][j].getName());
                    }
                }
            }
        }

        FileWriter toCurrent = new FileWriter(currentSave);

        for (String line : fileContent) {
            toCurrent.write(line + "\n");
        }
        toCurrent.close();

        if (!currentSave.getName().equals(saveFolderDir + fileName) && fileName != null) {
            FileWriter toFileName;
            if (fileName.equals(gameRunFileName)) {
                toFileName = new FileWriter(gameRunFileName);
            }
            else {
                toFileName = new FileWriter(saveFolderDir + fileName);
            }

            for (String line : fileContent) {
                toFileName.write(line + "\n");
            }
            toFileName.close();
        }

        System.out.println("Successfully saved to " + currentSave.getName() + " and " + saveFolderDir + fileName);
    }

    public static Vector[] ExtractAllLoadedSectionPos(String line) {

        Vector[] allLoadedSectionPos;
        char[] charArray = line.toCharArray();
        ArrayList<String> tmpscopes = new ArrayList<>();
        String tmpCharIntake = "";
        for (int i = 0; i < charArray.length; i++) {

            if (charArray[i] == ')') {
                tmpscopes.add(tmpCharIntake);
                tmpCharIntake = "";
                continue;
            }

            tmpCharIntake = tmpCharIntake + charArray[i];
        }
        allLoadedSectionPos = new Vector[tmpscopes.size()];

        for (int i = 0; i < tmpscopes.size(); i++) {
            String tmp2 = tmpscopes.get(i).replace('(', '\0');
            String[] tmp3 = tmp2.split(",");
            allLoadedSectionPos[i] = new Vector();
            if (tmp3[0] != "" && tmp3[1] != "") {
                allLoadedSectionPos[i].SetVector(Integer.parseInt(tmp3[0].trim()), Integer.parseInt(tmp3[1].trim()));
            }
        }

        return allLoadedSectionPos;
    }

    public static ArrayList<Section> LoadEverythingFromFile() throws FileNotFoundException {
        if (currentSave == null) {
            RefreshSaveFiles();
        }

        String[] fileContent = GetAllFromFile(currentSave).toArray(new String[0]);
        ArrayList<Section> retArray = new ArrayList<>();

        for (int i = 1; i < fileContent.length; i++) {
            if (fileContent[i].charAt(0) == '*') {

                String tmpPos = fileContent[i].substring(2);
                tmpPos = tmpPos.replace(')', '\0');
                String[] tmpSectionPos = tmpPos.split(",");
                Vector sectionPos = new Vector(Integer.parseInt(tmpSectionPos[0].trim()),
                        Integer.parseInt(tmpSectionPos[1].trim()));

                if (i == fileContent.length - 1) {
                    break;
                }
                if (fileContent[i + 1].charAt(0) == '*') {
                    continue;
                }
                Section section = new Section(sectionPos);
                i++;
                while (true) {

                    String cellInfo = fileContent[i];
                    Vector posInSection = ExtractPosFromCellInfo(cellInfo);
                    String cellName = cellInfo.replace(posInSection.FormatToBrackets(), "");
                    Cell targetCell = section.GetCell(posInSection);

                    if (LoadingManager.CheckIfNameInComponents(cellName)) {
                        targetCell.component = ComponentClass.GetComponents(cellName);
                        targetCell.component.Init(targetCell);
                        if (targetCell.component instanceof Starters) {
                            userInput.starter = (Starters) targetCell.component;
                        }
                    } else if (LoadingManager.CheckIfNameInEntities(cellName)) {
                        targetCell.entity = EntityClass.GetEntities(cellName);
                        targetCell.entity.Init(targetCell);
                    }

                    if (i == fileContent.length - 1) {
                        retArray.add(section);
                        break;
                    }
                    if (fileContent[i + 1].charAt(0) == '*') {
                        retArray.add(section);
                        break;
                    }
                    i++;
                }

            }
        }

        MapManager.loadedSections.clear();
        for (Section section : retArray) {
            MapManager.AddLoadedSection(section);
        }

        System.out.println("Successfully load from " + currentSave.getName());
        return retArray;
    }

    public static Section LoadSectionFromFile(Vector sectionPos) throws FileNotFoundException {
        if (currentSave == null) {
            RefreshSaveFiles();
        }

        String[] fileContent = GetAllFromFile(currentSave).toArray(new String[0]);

        boolean foundInLoaded = false;

        if (fileContent.length > 0) {
            Vector[] allLoadedSectionPos = ExtractAllLoadedSectionPos(fileContent[0]);
            for (Vector position : allLoadedSectionPos) {
                if (sectionPos.equals(position)) {
                    foundInLoaded = true;
                    break;
                }
            }
        } else {
            return null;
        }

        if (foundInLoaded) {
            int i = 1;
            while (i < fileContent.length) {
                if (fileContent[i].charAt(0) == '*') {
                    if (fileContent[i].contains(sectionPos.FormatToBrackets())) {
                        if (fileContent[i + 1].charAt(0) == '*') {
                            return null;
                        }
                        Section retSection = new Section(sectionPos);

                        i++;
                        while (i < fileContent.length) {
                            String cellInfo = fileContent[i];
                            Vector posInSection = ExtractPosFromCellInfo(cellInfo);
                            String cellName = cellInfo.replace(posInSection.FormatToBrackets(), "");
                            Cell targetCell = retSection.GetCell(posInSection);

                            if (LoadingManager.CheckIfNameInComponents(cellName)) {
                                targetCell.component = ComponentClass.GetComponents(cellName);
                                targetCell.component.Init(targetCell);
                            } else if (LoadingManager.CheckIfNameInEntities(cellName)) {
                                targetCell.entity = EntityClass.GetEntities(cellName);
                                targetCell.entity.Init(targetCell);
                            }

                            if (i == fileContent.length - 1) {
                                return retSection;
                            }
                            if (fileContent[i + 1].charAt(0) == '*') {
                                return retSection;
                            }
                            i++;
                        }

                    }
                }
                i++;
            }
        }

        return null;
    }

    public static Vector ExtractPosFromCellInfo(String cellInfo) {
        String tmpCharIntake = "";
        for (int i = 0; i < cellInfo.length(); i++) {
            if (cellInfo.charAt(i) != ')') {
                tmpCharIntake += cellInfo.charAt(i);
            } else {
                tmpCharIntake = tmpCharIntake.replace("(", "");
                String[] tmpNumAndComma = tmpCharIntake.split(",");
                return new Vector(Integer.parseInt(tmpNumAndComma[0].trim()),
                        Integer.parseInt(tmpNumAndComma[1].trim()));
            }
        }
        return null;
    }

    public static boolean CheckIfSectionInFileIsEmpty(Vector sectionPos) throws FileNotFoundException {
        if (currentSave == null) {
            RefreshSaveFiles();
        }

        ArrayList<String> fileContent = GetAllFromFile(currentSave);

        boolean foundInLoaded = false;

        if (fileContent.size() > 0) {
            Vector[] allLoadedSectionPos = ExtractAllLoadedSectionPos(fileContent.get(0));
            for (Vector loadedPosition : allLoadedSectionPos) {
                if (sectionPos.equals(loadedPosition)) {
                    foundInLoaded = true;
                    break;
                }
            }
        } else {
            return true;
        }

        if (!foundInLoaded) {
            return true;
        } else {
            int i = 1;
            while (i < fileContent.size()) {
                String tmp = fileContent.get(i);
                if (tmp.charAt(0) == '*') {
                    if (tmp.contains(sectionPos.FormatToBrackets())) {
                        if (++i < fileContent.size()) {
                            if (fileContent.get(i).charAt(0) == '*') {
                                return true;
                            } else {
                                return false;
                            }
                        } else {
                            return true;
                        }
                    }
                }
                i++;
            }
        }

        return true;

    }

    public static ArrayList<String> GetAllFromFile(File file) throws FileNotFoundException {
        if (file == null) {
            System.err.println("File doesn't exist.");
            return null;
        }

        Scanner scanner = new Scanner(file);
        ArrayList<String> retList = new ArrayList<>();

        while (scanner.hasNextLine()) {
            retList.add(scanner.nextLine());
        }

        scanner.close();
        return retList;

    }

    public static void RefreshSaveFiles() {

        allFileNames.clear();

        File saveFolder = new File("save\\");

        File[] listOfFiles = saveFolder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                allFileNames.add(listOfFiles[i].getName());
            }
        }

        if (currentSave == null && allFileNames.size() > 0) {
            currentSave = new File(saveFolderDir + allFileNames.get(0));
            currentSaveName = currentSave.getName();
        }
        if (currentSave == null) {
            try {
                currentSave = CreateNewSaveFile();
                currentSaveName = currentSave.getName();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        int i = 0;

        while (true) {
            for (int j = 0; j < allFileNames.size(); j++) {
                if (allFileNames.get(j).contains("newSave" + i)) {
                    break;
                }
                if (j == allFileNames.size() - 1) {
                    availableNewFileName = "newSave" + i;
                    System.out.println("Current save is: " + currentSave.getName());
                    return;
                }

            }
            i++;
        }
    }

    public static File CreateNewSaveFile() throws IOException {

        File newFile = new File(saveFolderDir + availableNewFileName);

        if (newFile.createNewFile()) {
            System.out.println(saveFolderDir + availableNewFileName + " Created.");
        }
        RefreshSaveFiles();

        return newFile;
    }

    public static void ChangeCurrentFile(String fileName) {
        if (fileName.equals(gameRunFileName)) {
            currentSave = gameRunFile;
            RefreshSaveFiles();
            return;
        }

        boolean exist = false;

        for (String name : allFileNames) {
            if (name.equals(fileName)) {
                exist = true;
                break;
            }
        }


        if (exist && !currentSave.getName().equals(saveFolderDir + fileName)) {
            currentSave = new File(saveFolderDir + fileName);
            currentSaveName = currentSave.getName();
            System.out.println("Current save has been changed to " + fileName);
            MapManager.loadedSections.clear();
        }

        RefreshSaveFiles();

    }

}
