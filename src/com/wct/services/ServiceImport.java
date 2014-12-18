package com.wct.services;

import java.io.IOException;
import jxl.read.biff.BiffException;
import java.io.InputStream;
import jxl.Cell;
import jxl.CellType;
import jxl.LabelCell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import com.wct.dao.DAOException;
import com.wct.dao.PersonDao;
import com.wct.entities.Person;
import com.wct.feedback.FeedbackPanelMsg;

public class ServiceImport {

	public static void importExcel(InputStream fileInputStream, FeedbackPanelMsg feedback) {
		try {
			Workbook workbook = Workbook.getWorkbook(fileInputStream);
			if (workbook.getNumberOfSheets() == 0) {
				feedback.error("le fichier excell est vide: il ne contient aucune feuille");
				return;
			}
			Sheet sheet = workbook.getSheet(0);
			if (! (sheet.getColumns() >= 3 && sheet.getRows() >=2) ) {
				feedback.error("le fichier excell est Non valide: Voir le modele");
				return;
			}
			Cell cellPrenomHeader = sheet.findCell("Prenom");
			if (cellPrenomHeader == null) {
				feedback.error("Prenom cell n'existe pas");
				return;
			}
			Cell cellNomHeader = sheet.findCell("Nom");
			if (cellNomHeader == null) {
				feedback.error("Nom cell n'existe pas");
				return;
			}
			Cell cellAgeHeader = sheet.findCell("Age");
			if (cellAgeHeader == null) {
				feedback.error("Age cell n'existe pas");
				return;
			}
			if ( !(cellPrenomHeader.getRow() == cellNomHeader.getRow() && cellAgeHeader.getRow() == cellNomHeader.getRow() && cellAgeHeader.getRow() == cellPrenomHeader.getRow())) {
				feedback.error("Nom Cell, Prenom Cell, Age Cell non aligné");
				return;
			}
			int index = cellPrenomHeader.getRow() + 1;
			for (int i = index; i < sheet.getRows(); i++) {
				Cell cellNom = sheet.getCell(cellNomHeader.getColumn(), i);
				Cell cellPrenom = sheet.getCell(cellPrenomHeader.getColumn(), i);
				Cell cellAge = sheet.getCell(cellAgeHeader.getColumn(), i);

				String nom = "";
				String prenom = "";
				int age = 0;
				if (cellNom.getType() == CellType.LABEL) {
					LabelCell lc = (LabelCell)cellNom;
					nom = lc.getString();
				}
				if (cellPrenom.getType() == CellType.LABEL) {
					LabelCell lc = (LabelCell)cellPrenom;
					prenom = lc.getString();
				}
				if (cellAge.getType() == CellType.NUMBER) {
					NumberCell nc = (NumberCell) cellAge;
					age = (int)nc.getValue();
				}
				
				try {
					PersonDao personDao = new PersonDao();
					Person p = new Person();
					p.setAge(age);
					p.setFirstName(prenom);
					p.setLastName(nom);
					personDao.create(p);
				} catch (DAOException e) {
					feedback.error(e.getMessage());
					continue;
				}
				
				System.out.println(" Nom : "+nom + " Prenom : " + prenom + " Age : "+age);
			}
		} catch (BiffException | IOException e) {
			feedback.error(" Error d'upload : " + e.getClass() + " Message : " + e.toString());
		}
		
	}

}
