
package org.mapfish.print.processor.http.matcher;

import org.apache.http.auth.AuthScope;
import org.springframework.http.HttpMethod;

import java.net.MalformedURLException;
import java.net.URI;

/**
 * Information required for performing a request match.
 *
 * @author Jesse on 9/5/2014.
 */
public final class MatchInfo {
    /**
     * A value representing all and any schemes.
     */
    public static final String ANY_SCHEME = null;
    /**
     * A value representing all and any hosts.
     */
    public static final String ANY_HOST = null;
    /**
     * A value representing all and any realms.
     */
    public static final String ANY_REALM = null;
    /**
     * A value representing all and any paths.
     */
    public static final String ANY_PATH = null;
    /**
     * A value representing all and any fragments.
     */
    public static final String ANY_FRAGMENT = null;
    /**
     * A value representing all and any queries.
     */
    public static final String ANY_QUERY = null;
    /**
     * A value representing all and any queries.
     */
    public static final HttpMethod ANY_METHOD = null;
    /**
     * A value representing all and any ports.
     */
    public static final int ANY_PORT = -1;

    private final String scheme;
    private final String host;
    private final int port;
    private final String path;
    private final String fragment;
    private final String query;
    private final String realm;
    private final HttpMethod method;

    /**
     * Constructor.
     *
     * @param scheme   the scheme to match.
     * @param host     the host to match.
     * @param port     the port to match.
     * @param path     the path to match.
     * @param fragment the fragment to match.
     * @param query    the query to match.
     * @param realm    the realm to match.
     * @param method   the method to match.
     */
    // CSOFF: ParameterNumber
    public MatchInfo(final String scheme,
                     final String host,
                     final int port,
                     final String path,
                     final String query,
                     final String fragment,
                     final String realm,
                     final HttpMethod method) {
        // CSON: ParameterNumber
        this.scheme = scheme;
        this.host = host;
        this.port = port;
        this.path = path;
        this.query = query;
        this.fragment = fragment;
        this.realm = realm;
        this.method = method;
    }

    /**
     * Create an info object from a uri and the http method object.
     *
     * @param uri    the uri
     * @param method the method
     */
    public static MatchInfo fromUri(final URI uri, final HttpMethod method) {
        int newPort = uri.getPort();
        if (newPort < 0) {
            try {
                newPort = uri.toURL().getDefaultPort();
            } catch (MalformedURLException e) {
                newPort = ANY_PORT;
            } catch (IllegalArgumentException e) {
                //the URL is relative
                newPort = ANY_PORT;
            }
        }

        return new MatchInfo(uri.getScheme(), uri.getHost(), newPort, uri.getPath(), uri.getQuery(),
                uri.getFragment(), ANY_REALM, method);
    }

    /**
     * Create an info object from an authscope object.
     *
     * @param authscope the authscope
     */
    @SuppressWarnings("StringEquality")
    public static MatchInfo fromAuthScope(final AuthScope authscope) {
        String newScheme = authscope.getScheme() == AuthScope.ANY_SCHEME ? ANY_SCHEME : authscope.getScheme();
        String newHost = authscope.getHost() == AuthScope.ANY_HOST ? ANY_HOST : authscope.getHost();
        int newPort = authscope.getPort() == AuthScope.ANY_PORT ? ANY_PORT : authscope.getPort();
        String newRealm = authscope.getRealm() == AuthScope.ANY_REALM ? ANY_REALM : authscope.getRealm();

        return new MatchInfo(newScheme, newHost, newPort, ANY_PATH, ANY_QUERY,
                ANY_FRAGMENT, newRealm, ANY_METHOD);
    }

    public String getScheme() {
        return this.scheme;
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    public String getPath() {
        return this.path;
    }

    public String getFragment() {
        return this.fragment;
    }

    public String getQuery() {
        return this.query;
    }

    public String getRealm() {
        return this.realm;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        if (this.realm != null) {
            sb.append('\'');
            sb.append(this.realm);
            sb.append('\'');
            sb.append(' ');
        }
        if (this.method != null) {
            sb.append(this.method);
            sb.append(' ');
        }
        if (this.scheme != null) {
            sb.append(this.scheme);
            sb.append("://");
        }
        if (this.host != null) {
            sb.append(this.host);
            if (this.port != -1) {
                sb.append(':');
                sb.append(this.port);
            }
        }
        if (this.path != null) {
            sb.append(this.path);
        }
        if (this.query != null) {
            sb.append('?');
            sb.append(this.query);
        }
        if (this.fragment != null) {
            sb.append('#');
            sb.append(this.fragment);
        }

        return sb.toString();
    }
}
