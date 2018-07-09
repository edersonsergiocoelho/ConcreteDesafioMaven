package br.com.concrete.bean;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "phone")
@NamedQueries({
    @NamedQuery(name = "Phone.findAll", query = "SELECT p FROM Phone p")})
public class Phone implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idphone")
    private Integer idphone;
    @Basic(optional = false)
    @Column(name = "number")
    private int number;
    @Basic(optional = false)
    @Column(name = "ddd")
    private int ddd;
    @JoinColumn(name = "usuario_iduser", referencedColumnName = "iduser")
    @ManyToOne(optional = false)
    private Usuario usuarioIduser;

    public Phone() {
    }

    public Phone(Integer idphone) {
        this.idphone = idphone;
    }

    public Phone(Integer idphone, int number, int ddd) {
        this.idphone = idphone;
        this.number = number;
        this.ddd = ddd;
    }

    public Integer getIdphone() {
        return idphone;
    }

    public void setIdphone(Integer idphone) {
        this.idphone = idphone;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getDdd() {
        return ddd;
    }

    public void setDdd(int ddd) {
        this.ddd = ddd;
    }

    public Usuario getUsuarioIduser() {
        return usuarioIduser;
    }

    public void setUsuarioIduser(Usuario usuarioIduser) {
        this.usuarioIduser = usuarioIduser;
    }

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ddd;
		result = prime * result + ((idphone == null) ? 0 : idphone.hashCode());
		result = prime * result + number;
		result = prime * result + ((usuarioIduser == null) ? 0 : usuarioIduser.hashCode());
		return result;
	}

    @Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Phone other = (Phone) obj;
		if (ddd != other.ddd)
			return false;
		if (idphone == null) {
			if (other.idphone != null)
				return false;
		} else if (!idphone.equals(other.idphone))
			return false;
		if (number != other.number)
			return false;
		if (usuarioIduser == null) {
			if (other.usuarioIduser != null)
				return false;
		} else if (!usuarioIduser.equals(other.usuarioIduser))
			return false;
		return true;
	}

    @Override
    public String toString() {
        return "br.com.concrete.bean.Phone[ idphone=" + idphone + " ]";
    }
    
}
