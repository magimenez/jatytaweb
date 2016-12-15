/**
 * 
 */
package com.crawljax.web.jatyta.plugins.util.form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.crawljax.core.state.StateVertex;

/**
 * The Form elements Value information used in the crawl.
 * 
 * @author mgimenez
 * 
 */
public class FormValuesInfo {
	/**
	 * The xpath for the form field.
	 */
	private String id;
	/**
	 * The count of valid values.
	 */
	private Integer validValuesCount = 0;
	/**
	 * The count of invalid values.
	 */
	private Integer invalidValuesCount = 0;
	/**
	 * The count of unknown values.
	 */
	private Integer unknownValuesCount = 0;
	/**
	 * The list of values for the form field with the state associated.
	 */
	private List<FormValuesInfoDetail> valuesDetail = new ArrayList<>();
	/**
	 * The map of values for every state found.
	 */
	private Map<Integer, List<Object>> valuesMap = new HashMap<Integer, 
			List<Object>>();
	/**
	 * The image of the form field.
	 */
	private byte[] image;
	/**
	 * The image of the label of the form field.
	 */
	private byte[] labelImage;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the image
	 */
	public byte[] getImage() {
		return image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(byte[] image) {
		this.image = image;
	}
	
	

	/**
	 * @return the labelImage
	 */
	public byte[] getLabelImage() {
		return labelImage;
	}

	/**
	 * @param labelImage the labelImage to set
	 */
	public void setLabelImage(byte[] labelImage) {
		this.labelImage = labelImage;
	}

	/**
	 * @return the validValuesCount
	 */
	public Integer getValidValuesCount() {
		validValuesCount = 0;
		for (FormValuesInfoDetail detail : valuesDetail) {
			if (FormValueType.VALID.equals(detail.getType())) {
				validValuesCount++;
			}
		}
		return validValuesCount;
	}

	/**
	 * @return the invalidValuesCount
	 */
	public Integer getInvalidValuesCount() {
		invalidValuesCount = 0;
		for (FormValuesInfoDetail detail : valuesDetail) {
			if (FormValueType.INVALID.equals(detail.getType())) {
				invalidValuesCount++;
			}
		}

		return invalidValuesCount;
	}

	/**
	 * @return the unknownValuesCount
	 */
	public Integer getUnknownValuesCount() {
		unknownValuesCount = 0;
		for (FormValuesInfoDetail detail : valuesDetail) {
			if (FormValueType.UNKNOWN.equals(detail.getType())) {
				unknownValuesCount++;
			}
		}
		return unknownValuesCount;
	}

	/**
	 * @return the valuesDetail
	 */
	public List<FormValuesInfoDetail> getValuesDetail() {
		return valuesDetail;
	}

	/**
	 * Add a new {@link FormValuesInfoDetail} with the parameters, to the
	 * valuesDetail list. if a value is associated to the state, don't add the
	 * parameters to the list.
	 * 
	 * @param type
	 *            The {@link FormValueType} parameter.
	 * @param value
	 *            The value parameter.
	 * @param state
	 *            Th {@link StateVertex} parameter.
	 */
	public void addNewDetail(FormValueType type, Object value, 
			StateVertex state) {

		FormValuesInfoDetail newDetail = new FormValuesInfoDetail();
		newDetail.setType(type);
		newDetail.setValue(value);
		newDetail.setStateAsociated(state.getId());
		LinkedList<Object> list;
		if (!valuesMap.containsKey(state.getId())) {
			//se crea la lista.
			list = new LinkedList<>();
		}else{
			// se verifica si el valor ya se agrego al values map.
			list = (LinkedList<Object>) valuesMap.get(
					state.getId());
		}
		list.add(value);
		valuesMap.put(state.getId(), list);
		//mgimenez 24/07/2016 setea el orden del value para el estado 
		newDetail.setValueOrder(list.size());
		valuesDetail.add(newDetail);
	}

	@Override
	public String toString() {

		String result = " id = " + getId() + " ; countInvalid = "
				+ getInvalidValuesCount() + " ; countValid = "
				+ getValidValuesCount() + " ; countUnknown = "
				+ getUnknownValuesCount() + " ; detailSize = "
				+ getValuesDetail().size();

		return result;
	}

	/**
	 * Returns a string with the follow content: [ detail.toString ] , ...
	 * 
	 * @return The String contain the values of every detail.
	 */
	public String toStringInfoDetail() {
		String result = "";
		String coma = "";
		for (FormValuesInfoDetail detail : this.valuesDetail) {
			result = result + coma + " [ " + detail.toString() + " ] ";
			coma = ",";
		}

		return result;
	}

	/**
	 * Obtain the {@link FormValuesInfoDetail} asociated to the
	 * {@link StateVertex} parameter
	 * 
	 * @param state
	 *            The {@link StateVertex} parameter.
	 * @return A {@link List} of {@link FormValuesInfoDetail}.
	 */
	public List<FormValuesInfoDetail> getDetailsByState(StateVertex state) {

		List<FormValuesInfoDetail> result = new ArrayList<>();
		for (FormValuesInfoDetail detail : this.valuesDetail) {
			if (detail.getStateAsociated().equals(state.getId())) {
				result.add(detail);
			}
		}

		return result;
	}
	
	/**
	 * Obtain the {@link FormValuesInfoDetail} asociated to the
	 * {@link StateVertex} parameter with the last orderValue.
	 * 
	 * @param state
	 *            The {@link StateVertex} parameter.
	 * @return A {@link List} of {@link FormValuesInfoDetail}.
	 */
	public FormValuesInfoDetail getLastOrderDetailByState(StateVertex state) {

		FormValuesInfoDetail result = null;
		int order = 0;
		for (FormValuesInfoDetail detail : this.valuesDetail) {
			if (detail.getStateAsociated().equals(state.getId())
					&& detail.getValueOrder() > order) {
				result =detail;
			}
		}
		return result;
	}
	
}
