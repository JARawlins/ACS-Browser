//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////
//
// Title:           County
// Files:           N/A
// Course:          CS400, Fall, 2019
//
// Author:          Joshua Rawlins
// Email:           jrawlins@wisc.edu
// Lecturer's Name: Andrew Kuemmel
//
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ///////////////////
//
// Partner Name: N/A
// Partner Email: N/A
// Partner Lecturer's Name: N/A
//
// VERIFY THE FOLLOWING BY PLACING AN X NEXT TO EACH TRUE STATEMENT:
// ___ Write-up states that pair programming is allowed for this assignment.
// ___ We have both read and understand the course Pair Programming Policy.
// ___ We have registered our team prior to the team registration deadline.
//
///////////////////////////// CREDIT OUTSIDE HELP /////////////////////////////
//
// Students who get help from sources other than their partner must fully
// acknowledge and credit those sources of help here. Instructors and TAs do
// not need to be credited here, but tutors, friends, relatives, room mates,
// strangers, and others do. If you received no outside help from either type
// of source, then please explicitly indicate NONE.
//
// Persons: None
// Online Sources: None 
/////////////////////////////// 80 COLUMNS WIDE ///////////////////////////////
package application;

public class County {
  /******************************** Variables *******************************/
  // county ID as a variable would be redundant information

  private int totalPop, men, women, numVotingAge, income, incomeError, incomePerCapita,
      incomePerCapitaError, employed;
  private String state, countyName;
  private double hispanic, white, black, nativePerson, asian, pacific, poverty, childPoverty,
      professional, service, office, construction, production, drive, carpool, transit, walk,
      otherTransport, workAtHome, meanCommute, privateWork, publicWork, selfEmployed, familyWork,
      unemployed;

  // calculated values
  private double sexRatio;
  private double realEmployment;
  private double whiteCollar;
  private double prPuRatio;

  // constructor
  public County() {
  }

  /******** Getters and Setters **********/
  public double getSexRatio() {
    return sexRatio;
  }

  public double getRealEmployment() {
    return realEmployment;
  }

  public double getWhiteCollar() {
    return whiteCollar;
  }

  public double getPrPuRatio() {
    return prPuRatio;
  }

  public int getTotalPop() {
    return totalPop;
  }

  public void setTotalPop(int totalPop) {
    this.totalPop = totalPop;
  }

  public int getMen() {
    return men;
  }

  public void setMen(int men) {
    this.men = men;
    this.sexRatio = (double) men / (double) women;
  }

  public int getWomen() {
    return women;
  }

  public void setWomen(int women) {
    this.women = women;
    this.sexRatio = (double) men / (double) women;
  }

  public int getNumVotingAge() {
    return numVotingAge;
  }

  public void setNumVotingAge(int numVotingAge) {
    this.numVotingAge = numVotingAge;
    realEmployment = (double) employed / (double) numVotingAge;
  }

  public int getIncome() {
    return income;
  }

  public void setIncome(int income) {
    this.income = income;
  }

  public int getIncomeError() {
    return incomeError;
  }

  public void setIncomeError(int incomeError) {
    this.incomeError = incomeError;
  }

  public int getIncomePerCapita() {
    return incomePerCapita;
  }

  public void setIncomePerCapita(int incomePerCapita) {
    this.incomePerCapita = incomePerCapita;
  }

  public int getIncomePerCapitaError() {
    return incomePerCapitaError;
  }

  public void setIncomePerCapitaError(int incomePerCapitaError) {
    this.incomePerCapitaError = incomePerCapitaError;
  }

  public int getEmployed() {
    return employed;
  }

  public void setEmployed(int employed) {
    this.employed = employed;
    realEmployment = (double) employed / (double) numVotingAge;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getCountyName() {
    return countyName;
  }

  public void setCountyName(String countyName) {
    this.countyName = countyName;
  }

  public double getHispanic() {
    return hispanic;
  }

  public void setHispanic(double hispanic) {
    this.hispanic = hispanic;
  }

  public double getWhite() {
    return white;
  }

  public void setWhite(double white) {
    this.white = white;
  }

  public double getBlack() {
    return black;
  }

  public void setBlack(double black) {
    this.black = black;
  }

  public double getNativePerson() {
    return nativePerson;
  }

  public void setNativePerson(double nativePerson) {
    this.nativePerson = nativePerson;
  }

  public double getAsian() {
    return asian;
  }

  public void setAsian(double asian) {
    this.asian = asian;
  }

  public double getPacific() {
    return pacific;
  }

  public void setPacific(double pacific) {
    this.pacific = pacific;
  }

  public double getPoverty() {
    return poverty;
  }

  public void setPoverty(double poverty) {
    this.poverty = poverty;
  }

  public double getChildPoverty() {
    return childPoverty;
  }

  public void setChildPoverty(double childPoverty) {
    this.childPoverty = childPoverty;
  }

  public double getProfessional() {
    return professional;
  }

  public void setProfessional(double professional) {
    this.professional = professional;
    whiteCollar = professional + office;
  }

  public double getService() {
    return service;
  }

  public void setService(double service) {
    this.service = service;
  }

  public double getOffice() {
    return office;
  }

  public void setOffice(double office) {
    this.office = office;
    whiteCollar = professional + office;
  }

  public double getConstruction() {
    return construction;
  }

  public void setConstruction(double construction) {
    this.construction = construction;
  }

  public double getProduction() {
    return production;
  }

  public void setProduction(double production) {
    this.production = production;
  }

  public double getDrive() {
    return drive;
  }

  public void setDrive(double drive) {
    this.drive = drive;
  }

  public double getCarpool() {
    return carpool;
  }

  public void setCarpool(double carpool) {
    this.carpool = carpool;
  }

  public double getTransit() {
    return transit;
  }

  public void setTransit(double transit) {
    this.transit = transit;
  }

  public double getWalk() {
    return walk;
  }

  public void setWalk(double walk) {
    this.walk = walk;
  }

  public double getOtherTransport() {
    return otherTransport;
  }

  public void setOtherTransport(double otherTransport) {
    this.otherTransport = otherTransport;
  }

  public double getWorkAtHome() {
    return workAtHome;
  }

  public void setWorkAtHome(double workAtHome) {
    this.workAtHome = workAtHome;
  }

  public double getMeanCommute() {
    return meanCommute;
  }

  public void setMeanCommute(double meanCommute) {
    this.meanCommute = meanCommute;
  }

  public double getPrivateWork() {
    return privateWork;
  }

  public void setPrivateWork(double privateWork) {
    this.privateWork = privateWork;
    prPuRatio = privateWork / publicWork;
  }

  public double getPublicWork() {
    return publicWork;
  }

  public void setPublicWork(double publicWork) {
    this.publicWork = publicWork;
    prPuRatio = privateWork / publicWork;
  }

  public double getSelfEmployed() {
    return selfEmployed;
  }

  public void setSelfEmployed(double selfEmployed) {
    this.selfEmployed = selfEmployed;
  }

  public double getFamilyWork() {
    return familyWork;
  }

  public void setFamilyWork(double familyWork) {
    this.familyWork = familyWork;
  }

  public double getUnemployed() {
    return unemployed;
  }

  public void setUnemployed(double unemployed) {
    this.unemployed = unemployed;
  }

}
