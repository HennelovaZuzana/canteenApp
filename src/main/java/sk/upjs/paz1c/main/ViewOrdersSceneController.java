package sk.upjs.paz1c.main;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;
import sk.upjs.paz1c.biznis.CanteenManager;
import sk.upjs.paz1c.biznis.DefaultCanteenManager;
import sk.upjs.paz1c.storage.DaoFactory;
import sk.upjs.paz1c.storage.EntityNotFoundException;
import sk.upjs.paz1c.storage.EntityUndeletableException;
import sk.upjs.paz1c.storage.Order;
import sk.upjs.paz1c.storage.OrderDao;

public class ViewOrdersSceneController {

	@FXML
	private DatePicker dayDatePicker;

	@FXML
	private ListView<Order> orderListView;

	@FXML
	private CheckBox preparedCheckBox;

	@FXML
	private CheckBox notPreparedCheckBox;

	@FXML
	private Button deleteButton;

	@FXML
	private Button prepareButton;

	private OrderDao orderDao = DaoFactory.INSTANCE.getOrderDao();
	private CanteenManager canteenManager = new DefaultCanteenManager();
	private Order selectedOrder;

	@FXML
	void initialize() {
		deleteButton.setDisable(true);
		prepareButton.setDisable(true);
		notPreparedCheckBox.setSelected(true);
		dayDatePicker.setDisable(true);
		updateListView();

		preparedCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				updateListView();
				if (newValue && notPreparedCheckBox.isSelected())
					dayDatePicker.setDisable(false);
				else
					dayDatePicker.setDisable(true);

			}
		});

		notPreparedCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				updateListView();
				if (newValue && preparedCheckBox.isSelected())
					dayDatePicker.setDisable(false);
				else
					dayDatePicker.setDisable(true);

			}
		});

		orderListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Order>() {

			@Override
			public void changed(ObservableValue<? extends Order> observable, Order oldValue, Order newValue) {
				if (newValue == null) {
					deleteButton.setDisable(true);
					prepareButton.setDisable(true);
				} else {
					deleteButton.setDisable(false);
					if (!newValue.isPrepared())
						prepareButton.setDisable(false);
					else
						prepareButton.setDisable(true);
				}
				selectedOrder = newValue;
			}
		});

		orderListView.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2) {
				CreateOrderSceneController controller = new CreateOrderSceneController(selectedOrder);
				openSaveOrderWindow(controller);
			}
		});

		// https://stackoverflow.com/questions/20383773/set-date-picker-time-format/21498568
		dayDatePicker.setConverter(new StringConverter<LocalDate>() {

			private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-M-d");

			@Override
			public String toString(LocalDate object) {
				if (object == null)
					return "";
				return dateTimeFormatter.format(object);
			}

			@Override
			public LocalDate fromString(String string) {
				if (string == null || string.trim().isEmpty())
					return null;

				return LocalDate.parse(string, dateTimeFormatter);
			}
		});

		dayDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
			try {
				LocalDateTime ldt = LocalDateTime.of(newValue, LocalTime.of(0, 0, 0));
				orderListView.setItems(FXCollections.observableArrayList(orderDao.getByDay(ldt)));
			} catch (EntityNotFoundException e) {
				orderListView.setItems(FXCollections.observableArrayList());
			}
		});

	}

	@FXML
	void closeWindow(ActionEvent event) {
		// https://stackoverflow.com/questions/29710492/how-can-i-fire-internal-close-request
		// because after closing window we want to update count of orders and window is not application modal
		Stage stage = (Stage) dayDatePicker.getScene().getWindow();
		stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
	}

	private void openSaveOrderWindow(CreateOrderSceneController controller) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("createOrderScene.fxml"));
			loader.setController(controller);
			Parent parent = loader.load();

			Scene scene = new Scene(parent);
			Stage stage = new Stage();
			stage.setTitle("Save Order");
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();

			updateListView();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void createNewOrder(ActionEvent event) {
		CreateOrderSceneController controller = new CreateOrderSceneController();
		openSaveOrderWindow(controller);
	}

	@FXML
	void deleteOrder(ActionEvent event) {
		try {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setContentText(
					"Do you really want to delete order for date: " + selectedOrder.getDay().toLocalDate());
			Optional<ButtonType> buttonType = alert.showAndWait();
			if (buttonType.get() == ButtonType.OK) {
				orderDao.delete(selectedOrder.getId());
				updateListView();
			}
		} catch (EntityUndeletableException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Selected order cannot be deleted, it has some food on it's list!");
			alert.show();
		}
	}

	@FXML
	void prepareSelectedOrder(ActionEvent event) {
		boolean enoughIngrs = canteenManager.checkOrderIngredientsAvailable(selectedOrder);
		if (!selectedOrder.isPrepared() && enoughIngrs) {
			selectedOrder.setPrepared(true);
			orderDao.save(selectedOrder);
			canteenManager.prepareIngredientsForOrder(selectedOrder);
			updateListView();
		} else if (!enoughIngrs) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("You don't have enough Ingredients for this order.");
			alert.show();
		}
	}

	private void updateListView() {
		List<Order> items = null;
		if (preparedCheckBox.isSelected() && notPreparedCheckBox.isSelected()) {
			items = orderDao.getAll();
		} else if (preparedCheckBox.isSelected()) {
			items = orderDao.getByPrepared(true);
		} else if (notPreparedCheckBox.isSelected()) {
			items = orderDao.getByPrepared(false);
		} else {
			items = new ArrayList<>();
		}
		orderListView.setItems(FXCollections.observableArrayList(items));
	}

}
