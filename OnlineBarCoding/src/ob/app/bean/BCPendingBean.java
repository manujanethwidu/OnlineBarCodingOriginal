package ob.app.bean;

import java.util.Date;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BCPendingBean {

	private final IntegerProperty sn;
	private final IntegerProperty pidNw;
	private final IntegerProperty pidOld;
	private final StringProperty tireDescpiton;
	private final StringProperty qg;
	private final BooleanProperty rebc;
	private final ObjectProperty<Date> bcDate;
	private final BooleanProperty updated;
	

	public BCPendingBean() {
		this(0, 0,0, null, null, null,false,false);
	}

	public BCPendingBean(int snx, int pidx,int pidox, String tireDespctionx, String qgx, Date bcDatex,boolean rebcx,boolean updatedx) {

		this.tireDescpiton = new SimpleStringProperty(tireDespctionx);
		this.sn = new SimpleIntegerProperty(snx);
		this.pidNw = new SimpleIntegerProperty(pidx);
		this.qg = new SimpleStringProperty(qgx);
		this.bcDate = new SimpleObjectProperty(bcDatex);
		this.rebc = new SimpleBooleanProperty(rebcx);
		this.pidOld = new SimpleIntegerProperty(pidox);
		this.updated = new SimpleBooleanProperty(updatedx);
	}

	public final IntegerProperty snProperty() {
		return this.sn;
	}
	

	public final int getSn() {
		return this.snProperty().get();
	}
	

	public final void setSn(final int sn) {
		this.snProperty().set(sn);
	}
	

	public final IntegerProperty pidNwProperty() {
		return this.pidNw;
	}
	

	public final int getPidNw() {
		return this.pidNwProperty().get();
	}
	

	public final void setPidNw(final int pidNw) {
		this.pidNwProperty().set(pidNw);
	}
	

	public final IntegerProperty pidOldProperty() {
		return this.pidOld;
	}
	

	public final int getPidOld() {
		return this.pidOldProperty().get();
	}
	

	public final void setPidOld(final int pidOld) {
		this.pidOldProperty().set(pidOld);
	}
	

	public final StringProperty tireDescpitonProperty() {
		return this.tireDescpiton;
	}
	

	public final String getTireDescpiton() {
		return this.tireDescpitonProperty().get();
	}
	

	public final void setTireDescpiton(final String tireDescpiton) {
		this.tireDescpitonProperty().set(tireDescpiton);
	}
	

	public final StringProperty qgProperty() {
		return this.qg;
	}
	

	public final String getQg() {
		return this.qgProperty().get();
	}
	

	public final void setQg(final String qg) {
		this.qgProperty().set(qg);
	}
	

	public final BooleanProperty rebcProperty() {
		return this.rebc;
	}
	

	public final boolean isRebc() {
		return this.rebcProperty().get();
	}
	

	public final void setRebc(final boolean rebc) {
		this.rebcProperty().set(rebc);
	}
	

	public final ObjectProperty<Date> bcDateProperty() {
		return this.bcDate;
	}
	

	public final Date getBcDate() {
		return this.bcDateProperty().get();
	}
	

	public final void setBcDate(final Date bcDate) {
		this.bcDateProperty().set(bcDate);
	}

	public final BooleanProperty updatedProperty() {
		return this.updated;
	}
	

	public final boolean isUpdated() {
		return this.updatedProperty().get();
	}
	

	public final void setUpdated(final boolean updated) {
		this.updatedProperty().set(updated);
	}
	
	



}
