package eu.printingin3d.javascad.utils;

import static eu.printingin3d.javascad.testutils.AssertEx.assertEqualsWithoutWhiteSpaces;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.models.ConstsTest;
import eu.printingin3d.javascad.models.IModel;
import eu.printingin3d.javascad.testutils.FileMockBuilder;
import eu.printingin3d.javascad.testutils.TestModel;

/**
 * Tests SaveScadFiles class - it's more like an integration test
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class SaveScadFilesTest {
	private static final File ROOT = new File("C:/temp/scadTest");

	@AfterClass
	public static void cleanUp() {
		deleteRecursive(ROOT);
	}
	
	/**
     * By default File#delete fails for non-empty directories, it works like "rm". 
     * We need something a little more brutual - this does the equivalent of "rm -r"
     * @param path Root File Path
     * @return true if the file and all sub files/directories have been removed
     * @throws FileNotFoundException
     */
    public static boolean deleteRecursive(File path) {
        if (!path.exists()) {
			return true;
		}
        boolean ret = true;
        if (path.isDirectory()){
            for (File f : path.listFiles()){
                ret = ret && deleteRecursive(f);
            }
        }
        return ret && path.delete();
    }
    
    public static String readTheWholeFile(File file) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		try {
			StringBuilder sb = new StringBuilder();
			boolean first = true;
			while (true) {
				String line = bufferedReader.readLine();
				if (line == null) {
					break;
				}
				if (first) {
					first = false;
				} else {
					sb.append("\n");
				} 
				sb.append(line);
			}
			return sb.toString();
		}
		finally {
			bufferedReader.close();
		}
    }
    
	@Test(expected=IllegalValueException.class)
	public void shouldThrowIllegalValueExceptionIfTheRootIsNull() {
		new SaveScadFiles(null);
	}

	@Test(expected=IllegalValueException.class)
	public void shouldThrowIllegalValueExceptionIfTheRootIsNotADirectory() {
		File root = new FileMockBuilder().isAFile().build();
		new SaveScadFiles(root);
	}
	
	@Test
	public void shouldDoNothingIfNoFileWasAdded() throws IOException {
		File root = new FileMockBuilder().isADirectory().build();
		new SaveScadFiles(root).saveScadFiles();
	}
	
	@Test
	public void shouldFileExistsAndContainsTheModelAfterSaving1() throws IOException {
		final String model = "(model)";
		
		SaveScadFiles saveScadFiles = new SaveScadFiles(ROOT);
		final File fileName = new File(ROOT.getAbsolutePath()+"/fileName1.scad");
		Assert.assertFalse(fileName.exists());
		saveScadFiles.addScadFile(new IScadFile() {
			@Override
			public Collection<IModel> getModels() {
				return Arrays.<IModel>asList(new TestModel(model));
			}
			
			@Override
			public File getFile(File root) {
				return fileName;
			}
		});
		saveScadFiles.saveScadFiles();
		Assert.assertTrue(fileName.exists());
		assertEqualsWithoutWhiteSpaces(model, readTheWholeFile(fileName));
	}
	
	@Test
	public void shouldFileExistsAndContainsTheModelAfterSaving2() throws IOException {
		final String model = "(model)";
		
		SaveScadFiles saveScadFiles = new SaveScadFiles(ROOT);
		final File fileName = new File(ROOT.getAbsolutePath()+"/fileName2.scad");
		Assert.assertFalse(fileName.exists());
		saveScadFiles.addModels("fileName2.scad", Arrays.<IModel>asList(new TestModel(model)));
		saveScadFiles.saveScadFiles();
		Assert.assertTrue(fileName.exists());
		assertEqualsWithoutWhiteSpaces(model, readTheWholeFile(fileName));
	}
	
	@Test
	public void shouldFileExistsAndContainsTheModelAfterSaving3() throws IOException {
		final String model = "(model)";
		
		SaveScadFiles saveScadFiles = new SaveScadFiles(ROOT);
		final File fileName = new File(ROOT.getAbsolutePath()+"/fileName3.scad");
		Assert.assertFalse(fileName.exists());
		saveScadFiles.addModel("fileName3.scad", new TestModel(model));
		saveScadFiles.saveScadFiles();
		Assert.assertTrue(fileName.exists());
		assertEqualsWithoutWhiteSpaces(ConstsTest.DEFAULT_CONSTS+" "+model, readTheWholeFile(fileName));
	}
	
	@Test
	public void shouldFileExistsAndBeEmptyIfThereIsNoModelInScadFile() throws IOException {
		SaveScadFiles saveScadFiles = new SaveScadFiles(ROOT);
		final File fileName = new File(ROOT.getAbsolutePath()+"/fileName4.scad");
		Assert.assertFalse(fileName.exists());
		saveScadFiles.addModels("fileName4.scad", Collections.<IModel>emptyList());
		saveScadFiles.saveScadFiles();
		Assert.assertTrue(fileName.exists());
		assertEqualsWithoutWhiteSpaces("", readTheWholeFile(fileName));
	}
}
