package com;

import java.util.List;
import javax.ejb.Remote;

@Remote
public interface CDDao {
    public void insert(CD cd);
    public List<CD> getAll();
    public CD getOne(float id);
    public CD update(CD cd);
    public void delete(CD cd);
    public List<CD> getEmpruntCDs();
    public CD setEmpruntCD(CD cd);
}
