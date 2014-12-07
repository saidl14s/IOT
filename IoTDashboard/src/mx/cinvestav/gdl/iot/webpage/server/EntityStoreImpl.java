package mx.cinvestav.gdl.iot.webpage.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.cinvestav.gdl.iot.webpage.client.DatabaseException;
import mx.cinvestav.gdl.iot.webpage.client.EntityStoreService;
import mx.cinvestav.gdl.iot.webpage.dao.DAO;
import mx.cinvestav.gdl.iot.webpage.dao.IoTEntity;
import mx.cinvestav.gdl.iot.webpage.dao.IoTProperty;
import mx.cinvestav.gdl.iot.webpage.dto.IoTEntityDTO;
import mx.cinvestav.gdl.iot.webpage.dto.IoTPropertyDTO;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class EntityStoreImpl extends RemoteServiceServlet implements EntityStoreService
{
	private static final long serialVersionUID = -8306702743270115220L;
	Logger logger = Logger.getLogger(EntityStoreImpl.class.getName());
	Mapper mapper = new DozerBeanMapper(Arrays.asList(new String[] { "dozer-bean-mappings.xml" }));

	public EntityStoreImpl()
	{
		super();
		System.setProperty("dozer.debug", "true");
	}

	@Override
	public void storeEntity(IoTEntityDTO entityDTO, Collection<? extends IoTPropertyDTO> propDTOList)
			throws DatabaseException
	{
		try
		{
			IoTEntity entity = mapper.map(entityDTO, IoTEntity.class);
			Collection<IoTProperty> propList = new ArrayList<>();
			for (IoTPropertyDTO prop : propDTOList)
			{
				propList.add(mapper.map(prop, IoTProperty.class));
			}
			DAO.insertEntity(entity, propList);
		}
		catch (DatabaseException e)
		{
			String message = "Exception in storeEntity: " + e.getMessage();
			logger.log(Level.SEVERE, message, e);
			throw e;
		}
	}

	@Override
	public List<IoTEntityDTO> getEntity(IoTEntityDTO entityDTO, Integer id) throws DatabaseException
	{
		try
		{
			//map to non DTO object
			IoTEntity entity = mapper.map(entityDTO, IoTEntity.class);

			// perform query
			List<? extends IoTEntity> entityList = DAO.getEntity(entity.getClass(), id);

			//map back to DTO
			List<IoTEntityDTO> propDTOList = new ArrayList<>();
			for (IoTEntity result : entityList)
			{
				IoTEntityDTO dto = mapper.map(result, IoTEntityDTO.class);
				propDTOList.add(dto);
			}
			return propDTOList;
		}
		catch (DatabaseException e)
		{
			String message = "Exception in getEntity: " + e.getMessage();
			logger.log(Level.SEVERE, message, e);
			throw e;
		}
	}

	@Override
	public List<IoTPropertyDTO> getProperties(IoTPropertyDTO entityDTO, Integer id)
			throws DatabaseException
	{
		//map to non DTO object
		IoTProperty entity = mapper.map(entityDTO, IoTProperty.class);
		try
		{
			List<? extends IoTProperty> properties = DAO.getProperties(entity.getClass(), id);

			//map back to DTO
			List<IoTPropertyDTO> propDTOList = new ArrayList<>();
			for (IoTProperty result : properties)
			{
				IoTPropertyDTO dto = mapper.map(result, IoTPropertyDTO.class);
				propDTOList.add(dto);
			}
			return propDTOList;
		}
		catch (DatabaseException e)
		{
			String message = "Exception in getEntity: " + e.getMessage();
			logger.log(Level.SEVERE, message, e);
			throw e;
		}
	}
}