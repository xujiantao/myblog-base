package me.jiantao.po;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Data;
import me.jiantao.common.AutoConvert;

@MappedSuperclass
@Data
public class BaseEntity<ID> {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	@AutoConvert
	protected ID id;

	@Column(updatable = false, name = "create_date", nullable=false)
	@AutoConvert
	protected long createDate;

}
