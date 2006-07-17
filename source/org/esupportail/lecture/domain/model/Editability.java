package org.esupportail.lecture.domain.model;

/**
 * Editability mode of an element :
 * - no : no editibility
 * - personal : only personal element creation
 * - managed : only managed eleemnt importation
 * - all : personal element creation and managed element importaion
 * @author gbouteil
 *
 */
public enum Editability { NO,PERSONAL,MANAGED,ALL };