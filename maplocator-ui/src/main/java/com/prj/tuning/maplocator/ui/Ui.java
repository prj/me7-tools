package com.prj.tuning.maplocator.ui;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.TreeMap;

import javax.xml.bind.JAXBException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.prj.tuning.maplocator.export.dotecu.DotECUExport;
import com.prj.tuning.maplocator.export.dotecu.model.*;
import com.prj.tuning.maplocator.export.ecuvars.ECUVariablesExport;
import com.prj.tuning.maplocator.export.xdf.XdfExport;
import com.prj.tuning.maplocator.model.LocatedMap;
import com.prj.tuning.maplocator.plugin.PluginCallback;
import com.prj.tuning.maplocator.plugin.PluginManager;
import com.prj.tuning.maplocator.util.FileUtil;
import com.prj.tuning.maplocator.util.Logger;

public class Ui {

	private static Label status;
	private Text fileName;
	private Table mapTable;
	private int callbacksLeft;
	private Button xdfDumpButton;
	private Button kpDumpButton;
	private Button ecuvariablesDumpButton;
	private Button ecuFileDumpButton;
	private byte[] binFile;
	
	private static Logger log = new Logger(Ui.class);
	
	private Collection<LocatedMap> currentMaps = Collections
			.synchronizedSet(new HashSet<LocatedMap>());
	private Shell shell;

	public void run() {
		Display display = new Display();
		shell = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN);
		shell.setText("Map Locator");
		shell.setBounds(100, 50, 500, 600);

		GridLayout layout = new GridLayout(2, false);
		shell.setLayout(layout);

		fileName = new Text(shell, SWT.READ_ONLY | SWT.BORDER);
		fileName.setText("Select binary...");
		fileName.setLayoutData(new GridData(GridData.FILL, SWT.CENTER, true,
				false));

		Button browse = new Button(shell, SWT.NONE);
		browse.setText("Browse...");
		browse.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));

		mapTable = new Table(shell, SWT.BORDER | SWT.HIDE_SELECTION);
		GridData tableGridData = new GridData(GridData.FILL, GridData.FILL,
				true, true);
		tableGridData.verticalSpan = 4;
		mapTable.setLayoutData(tableGridData);
		mapTable.setHeaderVisible(true);
		mapTable.setLinesVisible(true);

		TableColumn idCol = new TableColumn(mapTable, SWT.NONE);
		idCol.setText("Map ID");
		idCol.setResizable(false);
		idCol.setMoveable(false);

		TableColumn addCol = new TableColumn(mapTable, SWT.NONE);
		addCol.setText("Address");
		addCol.setResizable(false);
		addCol.setMoveable(false);
		addCol.setWidth(100);

		final Button analyzeButton = new Button(shell, SWT.NONE);
		analyzeButton.setText("Analyze");
		analyzeButton.setEnabled(false);
		analyzeButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false,
				false));
		analyzeButton.addSelectionListener(new AnalyzeHandler());

		xdfDumpButton = new Button(shell, SWT.NONE);
		xdfDumpButton.setText("Dump .xdf");
		xdfDumpButton.setEnabled(false);
		xdfDumpButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false,
				false));
		xdfDumpButton.addSelectionListener(new XdfDumpHandler());

		/*
		kpDumpButton = new Button(shell, SWT.NONE);
		kpDumpButton.setText("Dump .kp");
		kpDumpButton.setEnabled(false);
		kpDumpButton
				.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));*/

		ecuvariablesDumpButton = new Button(shell, SWT.NONE);
		ecuvariablesDumpButton.setText("Dump ECUParameters");
		ecuvariablesDumpButton.setEnabled(false);
		ecuvariablesDumpButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP,
				false, false));
		ecuvariablesDumpButton
				.addSelectionListener(new ECUVariablesDumpHandler());

		ecuFileDumpButton = new Button(shell, SWT.NONE);
		ecuFileDumpButton.setText("Dump .ecu File");
		ecuFileDumpButton.setEnabled(false);
		ecuFileDumpButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP,
				false, false));
		ecuFileDumpButton
				.addSelectionListener(new DotEcuDumpHandler());
		
		Button aboutButton = new Button(shell, SWT.NONE);
		aboutButton.setText("About...");
		aboutButton.setEnabled(true);
		aboutButton.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false,
				false));

		aboutButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent paramSelectionEvent) {
				MessageBox box = new MessageBox(shell, SWT.ICON_INFORMATION
						| SWT.OK);
				box.setText("About map locator");
				box.setMessage("Map locator by prj\n\n"
						+ "http://prj-tuning.com\n\n" + "All rights reserved.");
				box.open();
			}

			public void widgetDefaultSelected(SelectionEvent paramSelectionEvent) {
			}
		});

		GridData statusGridData = new GridData(GridData.FILL, SWT.BOTTOM, true,
				false);
		statusGridData.horizontalSpan = 2;
		Label statusSep = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		statusSep.setLayoutData(statusGridData);

		status = new Label(shell, 0);
		statusGridData = new GridData(GridData.FILL, SWT.TOP, false, false);
		statusGridData.horizontalSpan = 2;
		status.setLayoutData(statusGridData);
		setStatus("Ready...");

		browse.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(shell, SWT.OPEN);
				dialog.setFilterExtensions(new String[] {"*.bin"});
				String file = dialog.open();
				if (file != null) {
					fileName.setText(file);
					analyzeButton.setEnabled(true);
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		shell.open();
		idCol.setWidth(mapTable.getClientArea().width - 100);

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	public static synchronized void setStatus(String text) {
		status.setText(text);
	}

	private class AnalyzeHandler implements SelectionListener {

		public void widgetSelected(SelectionEvent e) {
			try {
				mapTable.removeAll();
				currentMaps.clear();
				xdfDumpButton.setEnabled(false);
				setStatus("Analyzing...");
				binFile = FileUtil.readFile(fileName.getText());
				callbacksLeft = PluginManager.findMaps(
						binFile,
						new AnalyzeCallback());
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		}

		public void widgetDefaultSelected(SelectionEvent e) {
		}
	}

	private class AnalyzeCallback implements PluginCallback {
		public synchronized void handleMaps(
				Collection<? extends LocatedMap> maps) {
			currentMaps.addAll(maps);
			populateTable(maps);
			callbacksLeft--;

			if (callbacksLeft == 0) {
				if (currentMaps.size() > 0) {
					// Sort
					TreeMap<String, LocatedMap> sortedMaps = new TreeMap<String, LocatedMap>();
					for (LocatedMap map : currentMaps) {
						sortedMaps.put(map.getId(), map);
					}
					mapTable.removeAll();
					populateTable(sortedMaps.values());

					// Enable dump
					xdfDumpButton.setEnabled(true);
					ecuvariablesDumpButton.setEnabled(true);
					ecuFileDumpButton.setEnabled(true);
				}
				setStatus("Ready...");
			}
		}

		public void populateTable(Collection<? extends LocatedMap> maps) {
			for (LocatedMap map : maps) {
				TableItem item = new TableItem(mapTable, SWT.None);
				item.setText(new String[] { map.getId(),
						String.format("0x%X", map.getAddress()) });
			}
		}
	}

	private class XdfDumpHandler implements SelectionListener {
		public void widgetDefaultSelected(SelectionEvent e) {
		}

		public void widgetSelected(SelectionEvent e) {
			FileDialog dialog = new FileDialog(shell, SWT.SAVE);
			dialog.setFilterExtensions(new String[] { "*.xdf" });
			String file = dialog.open();
			if (file != null) {
				try {
					XdfExport.export(new File(file), currentMaps,
							(int) new File(fileName.getText()).length());
				} catch (JAXBException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	private class DotEcuDumpHandler implements SelectionListener {
		public void widgetDefaultSelected(SelectionEvent e) {
		}

		public void widgetSelected(SelectionEvent e) {
			FileDialog dialog = new FileDialog(shell, SWT.SAVE);
			dialog.setFilterExtensions(new String[] { "*.ecu" });
			String file = dialog.open();
			if (file != null) {
				try {
					ECUConfigData ecuData = new ECUConfigData();
					//TODO:Automatically fill in the ECU Config Data info
					ecuData.version = "1.20";
					ecuData.connect = "SLOW-0x11";
					ecuData.communicate = "HM0";
					ecuData.logSpeed = "56000";
					ecuData.hwNumber = "0";
					ecuData.swNumber = "0";
					ecuData.partNumber = "0";
					ecuData.swVersion = "0";
					ecuData.lang = "en";
					ecuData.engineId = "0";
					DotECUExport.export(new File(file), currentMaps, ecuData);
				} catch (JAXBException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	private class ECUVariablesDumpHandler implements SelectionListener {
		public void widgetDefaultSelected(SelectionEvent e) {
		}

		public void widgetSelected(SelectionEvent e) {
			FileDialog dialog = new FileDialog(shell, SWT.SAVE);
			dialog.setFilterExtensions(new String[] { "*.xml" });
			String file = dialog.open();
			if (file != null) {
				try {
					ECUVariablesExport.export(new File(file), currentMaps, getSWNumber());
				} catch (JAXBException e1) {
					e1.printStackTrace();
				}
			}
		}
		
		private String getSWNumber()
		{
			byte[] searchSW = {0x73, 0x77, 0x3A};
			byte[] searchSC = {0x53, 0x43, 0x3A};
			String swNumStr = "";
			swNumStr = getSWNumber(searchSW);
			if (!swNumStr.isEmpty())
			{
				
			}
			else
			{
				swNumStr = getSWNumber(searchSC);
			}
			return swNumStr;
		}
		private String getSWNumber(byte[] pattern)
		{
			int swLoc = indexOf(binFile, pattern);
			char[] swNum = new char[10];
			String swNumStr = "";
			if (swLoc > 0x17000 && swLoc < 0x19000)
			{
				int j = 0;
				for (int i = swLoc + 3; i < swLoc + 13; i++)
				{
					swNum[j] = (char) binFile[i];
					j++;
				}
				swNumStr = new String(swNum);
			}
			return swNumStr.trim();
		}
		
		/**
		 * Finds the first occurrence of the pattern in the text.
		 * http://stackoverflow.com/questions/1507780/searching-for-a-sequence-of-bytes-in-a-binary-file-with-java
		 */
		public int indexOf(byte[] data, byte[] pattern) {
			int[] failure = computeFailure(pattern);

			int j = 0;
			if (data.length == 0)
				return -1;

			for (int i = 0; i < data.length; i++) {
				while (j > 0 && pattern[j] != data[i]) {
					j = failure[j - 1];
				}
				if (pattern[j] == data[i]) {
					j++;
				}
				if (j == pattern.length) {
					return i - pattern.length + 1;
				}
			}
			return -1;
		}
		
		/**
		 * Computes the failure function using a boot-strapping process, where the
		 * pattern is matched against itself.
		 */
		private int[] computeFailure(byte[] pattern) {
			int[] failure = new int[pattern.length];

			int j = 0;
			for (int i = 1; i < pattern.length; i++) {
				while (j > 0 && pattern[j] != pattern[i]) {
					j = failure[j - 1];
				}
				if (pattern[j] == pattern[i]) {
					j++;
				}
				failure[i] = j;
			}

			return failure;
		}
	}
}
