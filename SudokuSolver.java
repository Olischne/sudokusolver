package com.olischne.sudokusolver;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class SudokuSolver {
	private int[] sudoku;

	public SudokuSolver(String file) {
		sudoku = new int[81];
		readSudoku(allFromFile(file));
		printSudoku();
	}

	private void readSudoku(String input) {
		String[] numbers = input.split("\r\n");
		for (int i = 0; i < 81; i++) {
			if (i > numbers.length) {
				sudoku[i] = 0;
			} else {
				try {
					sudoku[i] = Integer.parseInt(numbers[i]);
				} catch (NumberFormatException e) {
					sudoku[i] = 0;
				}
			}
		}
	}

	private void printSudoku() {
		for (int i = 0; i < 9; i++) {
			if (i % 3 == 0) {
				System.out.println();
			}
			for (int o = 0; o < 9; o++) {
				if (o % 3 == 0) {
					System.out.print(" ");
				}
				System.out.print(sudoku[o + i * 9] + " ");
			}
			System.out.println();
		}
	}

	public void solveSudoku() {
		solveSudokuHelper(0);
	}

	private void solveSudokuHelper(int index) {
		if (index == 81) {
			System.out.println("Done!");
			printSudoku();
			toFile();
			return;
		}

		if (sudoku[index] != 0) {
			solveSudokuHelper(index + 1);
		} else {
			for (int i = 1; i <= 9; i++) {
				if (checkSudoku(index, i)) {
					sudoku[index] = i;
					solveSudokuHelper(index + 1);
					sudoku[index] = 0;
				}
			}
		}
	}

	private boolean checkSudoku(int indexChanged, int number) {
		int column = indexChanged % 9;
		int row = indexChanged / 9;

		int boxStartX = (column / 3) * 3;
		int boxStartY = (row / 3) * 3;
		int boxX = 0;
		int boxY = 0;

		for (int i = 0; i < 9; i++) {
			// check the row
			if (sudoku[row * 9 + i] == number) {
				return false;
			}
			// check the column
			if (sudoku[i * 9 + column] == number) {
				return false;
			}

			// check the box
			if (boxX == 3) {
				boxX = 0;
				boxY++;
			}
			if (sudoku[(boxStartY + boxY) * 9 + (boxStartX + boxX)] == number) {
				return false;
			}
			boxX++;
		}

		return true;
	}

	private String allFromFile(String path) {
		Path p = FileSystems.getDefault().getPath(path);
		byte[] bytes;
		try {
			bytes = Files.readAllBytes(p);
			return new String(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	private void toFile() {
		try {
			FileWriter writer = new FileWriter("result.txt", true);
			for (int i = 0; i < 9; i++) {
				if (i % 3 == 0) {
					writer.write("\r\n");
				}
				for (int o = 0; o < 9; o++) {
					if (o % 3 == 0) {
						writer.write(" ");
					}
					writer.write(sudoku[o + i * 9] + " ");
				}
				writer.write("\r\n");
			}
			writer.write("\r\n");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println("Usage: enter file name");
		}
		SudokuSolver s = new SudokuSolver(args[0]);
		s.solveSudoku();
	}
}
