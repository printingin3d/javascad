package eu.printingin3d.javascad.testutils;

import java.io.File;

import org.easymock.EasyMock;

public class FileMockBuilder {
	private boolean isFile = true;
	private boolean isDirectory = false;
	private boolean isExists = true;
	private File parentFile = null;
	
	public File build() {
		File file = EasyMock.createMock(File.class);
		EasyMock.expect(Boolean.valueOf(file.isDirectory())).andStubReturn(Boolean.valueOf(isExists && isDirectory));
		EasyMock.expect(Boolean.valueOf(file.isFile())).andStubReturn(Boolean.valueOf(isExists && isFile));
		EasyMock.expect(Boolean.valueOf(file.exists())).andStubReturn(Boolean.valueOf(isExists));
		EasyMock.expect(file.getParentFile()).andStubReturn(parentFile);
		EasyMock.replay(file);
		return file;
	}
	
	public FileMockBuilder isAFile() {
		isFile = true;
		isDirectory = false;
		return this;
	}
	
	public FileMockBuilder isADirectory() {
		isFile = false;
		isDirectory = true;
		return this;
	}
	
	public FileMockBuilder withExists(boolean isExists) {
		this.isExists = isExists;
		return this;
	}
	
	public FileMockBuilder withParentFile(File parentFile) {
		this.parentFile = parentFile;
		return this;
	}
}
