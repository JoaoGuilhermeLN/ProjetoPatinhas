package model;

public class Adocao{
  
  private int idAdocao;
  private Animal animal;
  private Cliente cliente;
  private String dataAdocao;

  public Adocao(int idAdocao, String dataAdocao, Animal animal, Cliente cliente){
    this.idAdocao = idAdocao;
    this.dataAdocao = dataAdocao;
    this.animal = animal;
    this.cliente = cliente;
  }

  public int getIdAdocao(){
    return idAdocao;
  }

  public void setIdAdocao(int idAdocao){
    this.idAdocao = idAdocao;
  }

  public String getDataAdocao(){
    return dataAdocao;
  }

  public void setDataAdocao(String dataAdocao){
    this.dataAdocao = dataAdocao;
  }

  public Animal getAnimal(){
    return animal;
  }

  public void setAnimal(Animal animal){
    this.animal = animal;
  }

  public Cliente getCliente(){
    return cliente;
  }

  public void setCliente(Cliente cliente){
    this.cliente = cliente;
  }
  
}
