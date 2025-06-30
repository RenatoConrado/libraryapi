window.addEventListener('DOMContentLoaded', () => {
    const requestHandlerBasic = new BasicRequest(new URL('localhost:8080'));
    new HandleLogin(requestHandlerBasic)
        .init(
            document.getElementById('login-form'),
            document.getElementById('error-message'),
            function getCredentials() {
                /** @type {HTMLInputElement} */
                const username = document.getElementById('username');
                /** @type {HTMLInputElement} */
                const password = document.getElementById('password');
                return [username.value, password.value];
            }
        );
});

class AuthenticationError extends Error {
    constructor(message) {
        super(message);
        this.name = 'AuthenticationError';
    }
}

/** @interface RequestHandler - Interface para requisições. */
class RequestHandler {
    /**
     * @param {URL} url
     */
    constructor(url) {
        this.url = url;
    }

    /**
     * @param {string} username
     * @param {string} password
     * @throws {Error}
     * @returns {Promise<void>}
     */
    async submit(username, password) {
        throw new Error('submit() não implementado');
    }
}

/** Implementação da autenticação Basic. */
class BasicRequest extends RequestHandler {
    /** @throws {AuthenticationError | Error} */
    async submit(username, password) {
        const credentials = btoa(`${username}:${password}`);
        const response = await fetch(this.url.toString(), {
            method: 'GET',
            headers: {
                'Authorization': `Basic ${credentials}`
            }
        });
        if (response.status === 401) {
            throw new AuthenticationError('Usuário ou senha inválidos.');
        }
        if (response.status === 403) {
            throw new AuthenticationError('Sem permissão para este recurso.');
        }
        if (!response.ok) {
            throw new Error(`Outro: ${response.statusText}`);
        }
    }
}

/** Classe para lidar com o formulário de login. */
class HandleLogin {
    /** @param {RequestHandler} requestHandler */
    constructor(requestHandler) {
        this.requestHandler = requestHandler;
    }

    /**
     * Inicializa o formulário.
     * @param {HTMLFormElement} formElement
     * @param {HTMLElement} errorElement
     * @param {() => [string, string]} getCredentials
     */
    init(formElement, errorElement, getCredentials) {
        formElement.addEventListener('submit', async (event) => {
            event.preventDefault();
            const [username, password] = getCredentials();
            try {
                await this.requestHandler.submit(username, password);
                window.location.href = '/home';
            } catch (error) {
                errorElement.textContent = error.message;
            }
        });
    }
}