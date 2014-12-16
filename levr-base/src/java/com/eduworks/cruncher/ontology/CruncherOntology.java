package com.eduworks.cruncher.ontology;

import com.eduworks.ontology.Ontology;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.ContextEvent;
import com.eduworks.resolver.Cruncher;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.tdb.TDB;

public abstract class CruncherOntology extends Cruncher
{

	protected static final String TDB_ONTOLOGY = "tdbOntology";
	static ReadWrite lastRW;

	static synchronized Ontology getOntology(String ontologyId, Dataset tdbDataset, Context c)
	{
		Ontology o = null;
		String cId = (String) c.get("tdbOntologyId");

		o = (Ontology) c.get(TDB_ONTOLOGY);
		if (cId != null && cId.equals(ontologyId))
		{
			if (o != null && !o.getJenaModel().isClosed())
			{
				return o;
			}
		}

		o = Ontology.loadOntology(tdbDataset, ontologyId);

		c.put("tdbOntologyId", ontologyId);
		c.put(TDB_ONTOLOGY, o);

		final Ontology ont = o;

		if (lastRW == ReadWrite.WRITE)
			c.onFinally(new ContextEvent()
			{
				@Override
				public void go()
				{
					synchronized (ont)
					{
						if (!ont.getJenaModel().isClosed())
							ont.getJenaModel().close();
					}
				}
			});

		return o;
	}

	static synchronized Dataset getDataSet(String directory, ReadWrite rw, Context c)
	{
		Dataset d = null;
		d = (Dataset) c.get("tdbDataset");
		ReadWrite trw = (ReadWrite) c.get("tdbReadWrite");
		if (d != null && trw != null && trw.equals(rw) == false)
		{
			if (trw == ReadWrite.WRITE)
				d.commit();

			Ontology o = (Ontology) c.get(TDB_ONTOLOGY);
			o.getJenaModel().close();

			c.remove(TDB_ONTOLOGY);
			c.remove("tdbOntologyId");

			d = null;
		}
		if (d != null)
		{
			if (!d.isInTransaction())
			{
				d.begin(rw);
				lastRW = rw;
			}

			return d;
		}

		d = Ontology.setTDBLocation(directory);
		d.begin(rw);
		lastRW = rw;

		final Dataset e = d;
		c.put("tdbDataset", d);
		c.put("tdbReadWrite", rw);

		if (rw == ReadWrite.WRITE)
			c.onSuccess(new ContextEvent()
			{
				@Override
				public void go()
				{
					if (e.isInTransaction())
						e.commit();
				}
			});
		if (rw == ReadWrite.WRITE)
			c.onFailure(new ContextEvent()
			{
				@Override
				public void go()
				{
					if (e.isInTransaction())
						e.abort();
				}
			});
		if (rw == ReadWrite.WRITE)
			c.onFinally(new ContextEvent()
			{
				@Override
				public void go()
				{
					// e.close();
				}
			});
		return e;
	}

}
