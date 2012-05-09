package org.purl.wf4ever.astrotaverna.vo;

import java.text.SimpleDateFormat;

import org.purl.wf4ever.astrotaverna.vo.utils.HTMLPane;


import net.ivoa.xml.conesearch.v1.ConeSearch;
import net.ivoa.xml.conesearch.v1.Query;
import net.ivoa.xml.sia.v1.ImageSize;
import net.ivoa.xml.sia.v1.SimpleImageAccess;
import net.ivoa.xml.sia.v1.SkySize;
import net.ivoa.xml.slap.v0.SimpleLineAccess;
import net.ivoa.xml.ssa.v0.DataSource;
import net.ivoa.xml.ssa.v0.SimpleSpectralAccess;
import net.ivoa.xml.vodataservice.v1.HTTPQueryType;
import net.ivoa.xml.vodataservice.v1.InputParam;
import net.ivoa.xml.vodataservice.v1.ParamHTTP;
import net.ivoa.xml.voresource.v1.AccessURL;
import net.ivoa.xml.voresource.v1.Capability;
import net.ivoa.xml.voresource.v1.Contact;
import net.ivoa.xml.voresource.v1.Content;
import net.ivoa.xml.voresource.v1.Creator;
import net.ivoa.xml.voresource.v1.Curation;
import net.ivoa.xml.voresource.v1.Date;
import net.ivoa.xml.voresource.v1.Interface;
import net.ivoa.xml.voresource.v1.ResourceName;
import net.ivoa.xml.voresource.v1.Service;
import net.ivoa.xml.voresource.v1.Source;
import net.ivoa.xml.voresource.v1.Type;

public class VOServiceDetails extends HTMLPane {
	private static final long serialVersionUID = 1L;
	private StringBuffer message;

	protected void appendFormat(String formatStr, Object... args) {
		boolean foundAny = false;
		for (int i = 0; i < args.length; i++) {
			if (args[i] == null) {
				args[i] = "";
			} else {
				foundAny = true;
			}
		}
		if (!foundAny) {
			return;
		}
		message.append(String.format(formatStr, args));
	}

	protected void capability(Capability c) {
		boolean seenCap = false;
		for (Interface i : c.getInterface()) {
			if (!(i instanceof ParamHTTP)) {
				continue;
				// TODO: Handle WebService interface?
			}
			if (!seenCap) {
				seenCap = true;
				message.append("<h3>Service</h3>");
				appendFormat("<div><code>%s</code></div>", c.getStandardID());
				appendFormat("<div>%s</div>", c.getDescription());
				if (c instanceof ConeSearch) {
					ConeSearch cone = (ConeSearch) c;
					coneSearch(cone);
				}
				if (c instanceof SimpleLineAccess) {
					SimpleLineAccess sla = (SimpleLineAccess) c;
					simpleLineAccess(sla);
				}

				if (c instanceof SimpleImageAccess) {
					SimpleImageAccess sia = (SimpleImageAccess) c;
					simpleImageAccess(sia);
				}
				if (c instanceof SimpleSpectralAccess) {
					SimpleSpectralAccess spa = (SimpleSpectralAccess) c;
					simpleSpectralAccess(spa);
				}

			}
			paramHTTP((ParamHTTP) i);
		}
	}

	protected void coneSearch(ConeSearch cone) {
		message.append("<dl>");
		if (cone.isVerbosity()) {
			message.append("<dt>Verbose</dt>");
		}
		appendFormat("<dt>Maximum records</dt><dd>%s</dd>",
				cone.getMaxRecords());

		appendFormat("<dt>Maximum search radius</dt><dd>%s</dd>",
				cone.getMaxSR());

		if (cone.getTestQuery() != null) {
			message.append("<dt>Test query</dt><dd>");
			message.append("<dl>");
			Query tq = cone.getTestQuery();
			appendFormat("<dt>Catalogue</dt><dd>%s</dd>", tq.getCatalog());
			appendFormat("<dt>SR</dt><dd>%s</dd>", tq.getSr());
			appendFormat("<dt>DEC</dt><dd>%s</dd>", tq.getDec());
			appendFormat("<dt>RA</dt><dd>%s</dd>", tq.getRa());
			appendFormat("<dt>Extras</dt><dd>%s</dd>", tq.getExtras());
			appendFormat("<dt>Verb</dt><dd>%s</dd>", tq.getVerb());
			message.append("</dl>");
			message.append("</dd>");
		}

		message.append("</dl>");
	}

	protected void content(Content content) {
		message.append("<div><em>");
		for (Type t : content.getType()) {
			message.append(t.name());
			message.append(" ");
		}
		message.append("</em></div>");
		appendFormat("<div>%s</div>", content.getDescription());

		message.append("<dl>");
		Source source = content.getSource();
		if (source != null) {
			message.append("<dt>Source</dt>");
			appendFormat("<dd>%s <code>%s</code></dd>", source.getValue(),
					source.getFormat());
		}
		if (!content.getSubject().isEmpty()) {
			message.append("<dt>Subjects</dt>");
			for (String s : content.getSubject()) {
				appendFormat("<dd>%s</dd>", s);
			}
		}
	}

	protected void curation(Curation curation) {
		appendFormat("<dt>Publisher</dt><dd>%s</dd>", curation.getPublisher()
				.getValue());
		if (!curation.getContributor().isEmpty()) {
			message.append("<dt>Contributor</dt>");
			for (ResourceName contrib : curation.getContributor()) {
				appendFormat("<dd>%s</dd>", contrib.getValue());
			}
		}
		if (!curation.getCreator().isEmpty()) {
			message.append("<dt>Creator</dt>");
			for (Creator creator : curation.getCreator()) {
				appendFormat("<dd>%s</dd>", creator.getName().getValue());
			}
		}
		if (!curation.getCreator().isEmpty()) {
			message.append("<dt>Contact</dt>");
			for (Contact contact : curation.getContact()) {
				appendFormat("<dd>%s &lt;<a href='mailto:%s'>%s</a>&gt; "
						+ "<br><a href='tel:%s'>%s</a><br>%s</dd>", contact
						.getName().getValue(), contact.getEmail(),
						contact.getEmail(), contact.getTelephone(),
						contact.getTelephone(), contact.getAddress());
			}
		}
		appendFormat("<dt>Version</dt><dd>%s</dd>", curation.getVersion());
		if (!curation.getDate().isEmpty()) {
			message.append("<dt>Date</dt>");
			for (Date date : curation.getDate()) {
				appendFormat("<dd>%s %s</dd>", date.getValue(), date.getRole());
			}
		}
	}

	protected void inputParam(InputParam param) {
		message.append("<dt>");
		message.append(param.getName());
		message.append(" (");
		message.append(param.getUse());
		message.append(" )");
		message.append("</dt><dd>");
		if (param.getUnit() != null) {
			message.append(param.getUnit());
		}
		if (param.getDataType() != null) {
			message.append(" <code>");
			message.append(param.getDataType().getValue());
			message.append(" [");
			message.append(param.getDataType().getArraysize());
			message.append("]");
			message.append("</code>");
		}
		message.append("</dd>");
		appendFormat("<dd><em>%s</em></dd>", param.getDescription());
	}

	protected void moreInformation(Service service) {
		message.append("<h3>More information</h3><dl>");
		appendFormat("<dt>Identifier</dt><dd>%s</dd>", service.getIdentifier());
		appendFormat("<dt>Status</dt><dd>%s</dd>", service.getStatus());
		if (service.getUpdated() != null) {
			appendFormat(
					"<dt>Updated</dt><dd>%s</dd>",
					new SimpleDateFormat().format(service.getUpdated()
							.toGregorianCalendar().getTime()));
		}
		Curation curation = service.getCuration();
		if (curation != null) {
			curation(curation);
		}
		appendFormat("<dt>Reference</dt><dd><a href='%s'>%s</a></dd>", service
				.getContent().getReferenceURL(), service.getContent()
				.getReferenceURL());

		message.append("</dl>");
	}

	protected HTTPQueryType paramHTTP(ParamHTTP paramHTTP) {
		HTTPQueryType queryType = paramHTTP.getQueryType();
		if (queryType == null) {
			queryType = HTTPQueryType.GET;
		}
		for (AccessURL accessURL : paramHTTP.getAccessURL()) {
			message.append(String.format("<p><code>%s %s</code></p>",
					queryType, accessURL.getValue().trim()));
		}
		appendFormat("<div><em>Version: %s</em></div>", paramHTTP.getVersion());
		appendFormat("<div>Result type: <code>%s</code></div>",
				paramHTTP.getResultType());
		if (paramHTTP.getParam() != null && !paramHTTP.getParam().isEmpty()) {
			message.append("<h4>Input parameters</h4><dl>");
			for (InputParam param : paramHTTP.getParam()) {
				inputParam(param);
			}
			message.append("</dl>");
		}
		return queryType;
	}

	public void setService(Service service) {
		if (service == null) {
			reset();
			return;
		}
		message = new StringBuffer();
		appendFormat("<html><body><h2>%s: %s</h2>", service.getShortName(),
				service.getTitle());

		Content content = service.getContent();
		if (content != null) {
			content(content);
		}
		message.append("</dl>");

		for (Capability c : service.getCapability()) {
			capability(c);

		}
		moreInformation(service);
		message.append("</body></html>");

		setText(message.toString());
	}

	protected void simpleImageAccess(SimpleImageAccess sia) {
		message.append("<dl>");

		appendFormat("<dt>Maximum records</dt><dd>%s</dd>", sia.getMaxRecords());

		appendFormat("<dt>Image service type</dt><dd>%s</dd>",
				sia.getImageServiceType());
		if (sia.getMaxQueryRegionSize() != null) {
			SkySize size = sia.getMaxQueryRegionSize();
			message.append("<dt>Maximum query region size</dt>");
			appendFormat("<dd>Lat: %s, Long: %s</dd>", size.getLat(),
					size.getLong());
		}
		if (sia.getMaxImageExtent() != null) {
			SkySize size = sia.getMaxImageExtent();
			message.append("<dt>Maximum image extent</dt>");
			appendFormat("<dd>Lat: %s, Long: %s</dd>", size.getLat(),
					size.getLong());
		}
		if (sia.getMaxImageSize() != null) {
			ImageSize size = sia.getMaxImageSize();
			message.append("<dt>Maximum image size</dt>");
			appendFormat("<dd>Lat: %s, Long: %s</dd>", size.getLat(),
					size.getLong());
		}

		appendFormat("<dt>Maximum file size</dt><dd>%s</dd>",
				sia.getMaxFileSize());

		if (sia.getTestQuery() != null) {
			message.append("<dt>Test query</dt><dd>");
			message.append("<dl>");
			net.ivoa.xml.sia.v1.Query tq = sia.getTestQuery();
			appendFormat("<dt>POS</dt><dd>Lat: %s, Long: %s</dd>", tq.getPos()
					.getLat(), tq.getPos().getLong());
			appendFormat("<dt>SIZE</dt><dd>Lat: %s, Long: %s</dd>", tq
					.getSize().getLat(), tq.getSize().getLong());
			appendFormat("<dt>Extras</dt><dd>%s</dd>", tq.getExtras());
			appendFormat("<dt>Verb</dt><dd>%s</dd>", tq.getVerb());
			message.append("</dl>");
			message.append("</dd>");
		}
		message.append("</dl>");
	}

	protected void simpleLineAccess(SimpleLineAccess sla) {
		message.append("<dl>");

		appendFormat("<dt>Maximum records</dt><dd>%s</dd>", sla.getMaxRecords());
		appendFormat("<dt>Source</dt><dd>%s</dd>", sla.getDataSource());

		if (sla.getTestQuery() != null) {
			message.append("<dt>Test query</dt><dd>");
			message.append("<dl>");
			net.ivoa.xml.slap.v0.Query tq = sla.getTestQuery();
			appendFormat("<dt>Wavelength</dt><dd>%s - %s</dd>", tq
					.getWavelength().getMinWavelength(), tq.getWavelength()
					.getMaxWavelength());
			appendFormat("<dt>Query data cmd</dt><dd>%s</dd>",
					tq.getQueryDataCmd());
			message.append("</dl>");
			message.append("</dd>");
		}
		message.append("</dl>");
	}

	protected void simpleSpectralAccess(SimpleSpectralAccess spa) {
		message.append("<dl>");

		appendFormat("<dt>Maximum records</dt><dd>%s</dd>", spa.getMaxRecords());

		appendFormat("<dt>Maximum search radius</dt><dd>%s</dd>",
				spa.getMaxSearchRadius());
		appendFormat("<dt>Maximum aperture</dt><dd>%s</dd>",
				spa.getMaxAperture());

		appendFormat("<dt>Maximum file size</dt><dd>%s</dd>",
				spa.getMaxFileSize());
		if (!spa.getDataSource().isEmpty()) {
			message.append("<dt>Sources</dt>");
			for (DataSource source : spa.getDataSource()) {
				appendFormat("<dd>%s</dd>", source);
			}
		}

		if (!spa.getSupportedFrame().isEmpty()) {
			message.append("<dt>Supported frames</dt>");
			for (String frame : spa.getSupportedFrame()) {
				appendFormat("<dd>%s</dd>", frame);
			}
		}

		if (spa.getTestQuery() != null) {
			message.append("<dt>Test query</dt><dd>");
			message.append("<dl>");
			net.ivoa.xml.ssa.v0.Query tq = spa.getTestQuery();
			appendFormat("<dt>POS</dt><dd>Lat: %s, Long: %s</dd>", tq.getPos()
					.getLat(), tq.getPos().getLong());
			appendFormat("<dt>SIZE</dt><dd>%s</dd>", tq.getSize());
			appendFormat("<dt>Query data cmd</dt><dd>%s</dd>",
					tq.getQueryDataCmd());
			message.append("</dl>");
			message.append("</dd>");
		}
		message.append("</dl>");
	}
}
