package cn.cnyaoshun.from.common.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@MappedSuperclass
@ToString
public abstract class AbstractOnlyIdEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	@Getter @Setter	protected Long id;
}
