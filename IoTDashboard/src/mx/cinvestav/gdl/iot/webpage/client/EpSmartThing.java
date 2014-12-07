package mx.cinvestav.gdl.iot.webpage.client;

import java.util.ArrayList;
import java.util.Collection;

import mx.cinvestav.gdl.iot.dashboard.client.ClientConstants;
import mx.cinvestav.gdl.iot.webpage.dto.IoTPropertyDTO;
import mx.cinvestav.gdl.iot.webpage.dto.SmartThingDTO;
import mx.cinvestav.gdl.iot.webpage.dto.SmartThingPropertyDTO;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;


public class EpSmartThing implements EntryPoint {

	private TextBox tbOperationS = new TextBox();

	private DialogBox dialogBox = new DialogBox();
	private Button btClose = new Button("Close");
	private Button btError = new Button("Close");

	private VerticalPanel dialogPanel = new VerticalPanel();
	private Label lbDialogBox = new Label();

	private ListBox listNameProperty = new ListBox(true);
	private ListBox listValueProperty = new ListBox(true);
	private ListBox listActiveProperty = new ListBox(true);

	private TextBox name = new TextBox();
	private TextBox value = new TextBox();
	private CheckBox active = new CheckBox();

	private FormPanel form = new FormPanel();
	private VerticalPanel formPanel = new VerticalPanel();

	private Button btSaveSmartThing = new Button("Save SmartThing");
	private Button btCancelSmartThing = new Button("Cancel");
	private HorizontalPanel buttonsPanel = new HorizontalPanel();

	private FlexTable tableFields = new FlexTable();
	private TextBox tbId = new TextBox();
	private TextBox tbName = new TextBox();
	private TextBox tbDescription = new TextBox();

	private VerticalPanel propertyPanel = new VerticalPanel();
	private Label lbProperty = new Label();
	private FlexTable tableProperty = new FlexTable();

	private Button btAddProperty = new Button("Add");

	private VerticalPanel controllerPanel = new VerticalPanel();
	private Label lbController = new Label();
	private ListBox cbController = new ListBox();

	private ArrayList<String> property = new ArrayList<String>();
	private static final EntityStoreServiceAsync entityService = GWT
			.create(EntityStoreService.class);

	@Override
	public void onModuleLoad() {
		form.setAction(GWT.getHostPageBaseURL() + "addEntityServlet");

		form.setMethod(FormPanel.METHOD_POST);
		form.setEncoding(FormPanel.ENCODING_URLENCODED);

		tableFields.setText(0, 0, "Id: ");
		tableFields.setWidget(0, 1, tbId);

		tableFields.setText(1, 0, "Name: ");
		tableFields.setWidget(1, 1, tbName);

		tableFields.setText(2, 0, "Description: ");
		tableFields.setWidget(2, 1, tbDescription);

		tbId.setName("tbIdS");
		tbId.setEnabled(false);

		tbName.setName(ClientConstants.NAME);
		tbDescription.setName(ClientConstants.DESCRIPTION);

		listNameProperty.setName(ClientConstants.PROP_NAMES);
		listNameProperty.setVisible(false);
		listValueProperty.setName(ClientConstants.PROP_VALUES);
		listValueProperty.setVisible(false);
		listActiveProperty.setName(ClientConstants.PROP_ACTIVE);
		listActiveProperty.setVisible(false);

		tbOperationS.setName(ClientConstants.OPERATION);
		tbOperationS.setText(ClientConstants.SMART_THING);// Id Controller
		tbOperationS.setVisible(false);

		tableProperty.setText(0, 0, "Name");
		tableProperty.setText(0, 1, "Value");
		tableProperty.setText(0, 2, "Active");
		tableProperty.setText(0, 3, "       ");

		tableProperty.getCellFormatter().addStyleName(0, 0,
				"headerTableProperty");
		tableProperty.getCellFormatter().addStyleName(0, 1,
				"headerTableProperty");
		tableProperty.getCellFormatter().addStyleName(0, 2,
				"headerTableProperty");
		tableProperty.getCellFormatter().addStyleName(0, 3,
				"headerTableProperty");

		tableProperty.addStyleName("tableProperty");
		tableProperty.setCellPadding(3);

		lbProperty.setText("Property");
		lbProperty.setStyleName("lbProperty");

		propertyPanel.add(lbProperty);
		propertyPanel.add(btAddProperty);
		propertyPanel.add(tableProperty);

		// Add name to the combo that has the controller that belows to that
		// smartthing
		lbController.setText("Controller:");
		lbController.addStyleName("lbProperty");
		cbController.setName("cbControllerS");
		controllerPanel.add(lbController);
		controllerPanel.add(cbController);

		formPanel.add(tableFields);
		formPanel.add(propertyPanel);
		formPanel.add(controllerPanel);

		btSaveSmartThing.addStyleName("btSave");
		btCancelSmartThing.addStyleName("btSave");

		buttonsPanel.add(btSaveSmartThing);
		buttonsPanel.add(btCancelSmartThing);
		formPanel.add(buttonsPanel);
		formPanel.add(tbOperationS);
		formPanel.add(listNameProperty);
		formPanel.add(listValueProperty);
		formPanel.add(listActiveProperty);

		form.setWidget(formPanel);

		DecoratorPanel decoratorPanel = new DecoratorPanel();

		decoratorPanel.add(form);

		RootPanel.get("formContainer").add(decoratorPanel);

		dialogBox.add(btClose);

		btClose.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});

		btError.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});

		btAddProperty.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				btAddProperty.setEnabled(false);
				addProperty();
			}
		});

		// Add a 'submit' button.
		btSaveSmartThing.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				SmartThingDTO st = new SmartThingDTO();
				st.setName(tbName.getText());
				st.setDescription(tbDescription.getText());
				//TODO: set controller id
				st.setIdcontroller(0);

				Collection<IoTPropertyDTO> props = new ArrayList<>();
				for (int i = 0; i < listNameProperty.getItemCount(); i++) {
					IoTPropertyDTO prop = new SmartThingPropertyDTO();
					prop.setName(listNameProperty.getItemText(i));
					prop.setValue(listValueProperty.getItemText(i));
					prop.setActive(Boolean.valueOf(listActiveProperty
							.getValue(i)));
					props.add(prop);
				}

				entityService.storeEntity(st, props, new AsyncCallback<Void>() {
					@Override
					public void onSuccess(Void result) {
						dialogBox.setAnimationEnabled(true);
						dialogBox.setGlassEnabled(true);
						dialogBox.center();

						dialogBox.setText("Information");

						lbDialogBox.setText("SmartThing succesfully stored");
						dialogPanel.add(lbDialogBox);
						dialogPanel.setCellHorizontalAlignment(lbDialogBox,
								HasHorizontalAlignment.ALIGN_CENTER);

						dialogPanel.add(btClose);
						dialogPanel.setCellHorizontalAlignment(btClose,
								HasHorizontalAlignment.ALIGN_CENTER);

						dialogBox.add(dialogPanel);

						dialogBox.show();
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert(caught.getMessage());
					}
				});

			}
		});

		btCancelSmartThing.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Window.Location.replace("wpSmartThings.jsp");
			}
		});

	}

	private void addProperty() {
		int row = tableProperty.getRowCount();

		tableProperty.setWidget(row, 0, name);
		tableProperty.setWidget(row, 1, value);
		tableProperty.setWidget(row, 2, active);

		// Add a button to remove this stock from the table.
		Button saveProperty = new Button("Save");
		saveProperty.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				btAddProperty.setEnabled(true);
				saveProperty();
			}
		});

		Button cancelProperty = new Button("Cancel");

		cancelProperty.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				btAddProperty.setEnabled(true);
			}
		});

		HorizontalPanel buttonsPanel = new HorizontalPanel();
		buttonsPanel.add(saveProperty);
		buttonsPanel.add(cancelProperty);

		tableProperty.setWidget(row, 3, buttonsPanel);

	}

	private void saveProperty() {
		tableProperty.removeRow(tableProperty.getRowCount() - 1);

		final String symboln = name.getText();
		final String symbolv = value.getText();
		CheckBox symbola = new CheckBox();

		property.add(symboln);

		if (active.getValue() == true) {
			symbola.setValue(true);
		} else {
			symbola.setValue(false);
		}

		if (symboln.length() > 45) {
			dialogBox.setAnimationEnabled(true);
			dialogBox.setGlassEnabled(true);
			dialogBox.center();

			dialogBox.setText("Error");

			lbDialogBox.setText("The name must have less than 45 characters");
			dialogPanel.add(lbDialogBox);
			dialogPanel.setCellHorizontalAlignment(lbDialogBox,
					HasHorizontalAlignment.ALIGN_CENTER);

			dialogPanel.add(btError);
			dialogPanel.setCellHorizontalAlignment(btError,
					HasHorizontalAlignment.ALIGN_CENTER);

			dialogBox.add(dialogPanel);

			dialogBox.show();

			return;
		}

		if (symbolv.length() > 45) {
			dialogBox.setAnimationEnabled(true);
			dialogBox.setGlassEnabled(true);
			dialogBox.center();

			dialogBox.setText("Error");

			lbDialogBox.setText("The value must have less than 45 characters");
			dialogPanel.add(lbDialogBox);
			dialogPanel.setCellHorizontalAlignment(lbDialogBox,
					HasHorizontalAlignment.ALIGN_CENTER);

			dialogPanel.add(btError);
			dialogPanel.setCellHorizontalAlignment(btError,
					HasHorizontalAlignment.ALIGN_CENTER);

			dialogBox.add(dialogPanel);

			dialogBox.show();

			return;
		}
		name.setText("");
		value.setText("");
		active.setValue(false);

		int row = tableProperty.getRowCount();
		tableProperty.setText(row, 0, symboln);
		tableProperty.setText(row, 1, symbolv);
		tableProperty.setWidget(row, 2, symbola);

		listNameProperty.addItem(symboln);
		listValueProperty.addItem(symbolv);
		listActiveProperty.addItem(symbola.getValue() + "");

		Button removeProperty = new Button("Remove");

		removeProperty.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				int removedIndex = property.indexOf(symboln);
				property.remove(removedIndex);
				tableProperty.removeRow(removedIndex + 1);

				listNameProperty.removeItem(removedIndex);
				listValueProperty.removeItem(removedIndex);
				listActiveProperty.removeItem(removedIndex);

			}
		});

		tableProperty.setWidget(row, 3, removeProperty);
	}

}
