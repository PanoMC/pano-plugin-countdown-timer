{#if addon?.id === 'pano-plugin-countdown-timer'}
  <div class="card">
    <div class="card-header">
      {$_('settings.title')}
    </div>
    <div class="card-body">
      <!-- Enabled -->
      <div class="row mb-3">
        <label class="col-md-6 col-form-label" for="ct-enabled">
          <span class="d-block">
            {$_('settings.enabled')}
          </span>
          <small>
            {$_('settings.enabled-desc')}
          </small>
        </label>
        <div class="col-md-6 d-flex align-items-center">
          <div class="form-check form-switch">
            <input
              class="form-check-input"
              type="checkbox"
              id="ct-enabled"
              bind:checked={config.enabled} />
          </div>
        </div>
      </div>

      <!-- Title -->
      <div class="row mb-3">
        <label class="col-md-6 col-form-label" for="ct-title">
          {$_('settings.countdown-title')}
          <small class="d-block">{$_('settings.countdown-title-desc')}</small>
        </label>
        <div class="col-md-6">
          <input
            class="form-control"
            id="ct-title"
            type="text"
            bind:value={config.title} />
        </div>
      </div>

      <!-- Message -->
      <div class="row mb-3">
        <label class="col-md-6 col-form-label" for="ct-message">
          {$_('settings.message')}
          <small class="d-block">{$_('settings.message-desc')}</small>
        </label>
        <div class="col-md-6">
          <textarea class="form-control" id="ct-message" rows="2" bind:value={config.message}
          ></textarea>
        </div>
      </div>

      <hr />

      <!-- End Date -->
      <h5 class="mb-3">{$_('settings.timer-section')}</h5>

      <div class="row mb-3">
        <label class="col-md-6 col-form-label" for="ct-end-date">
          {$_('settings.end-date')}
          <small class="d-block">{$_('settings.end-date-desc')}</small>
        </label>
        <div class="col-md-6">
          <input
            class="form-control mb-2"
            id="ct-end-date"
            type="datetime-local"
            step="1"
            bind:value={endDateLocal} />
          <div class="d-flex flex-wrap gap-1">
            <button type="button" class="btn btn-sm btn-outline-secondary" onclick={() => setQuickDuration(15 * 60 * 1000)}>15{$_('settings.quick-min')}</button>
            <button type="button" class="btn btn-sm btn-outline-secondary" onclick={() => setQuickDuration(30 * 60 * 1000)}>30{$_('settings.quick-min')}</button>
            <button type="button" class="btn btn-sm btn-outline-secondary" onclick={() => setQuickDuration(60 * 60 * 1000)}>1{$_('settings.quick-hour')}</button>
            <button type="button" class="btn btn-sm btn-outline-secondary" onclick={() => setQuickDuration(3 * 60 * 60 * 1000)}>3{$_('settings.quick-hour')}</button>
            <button type="button" class="btn btn-sm btn-outline-secondary" onclick={() => setQuickDuration(6 * 60 * 60 * 1000)}>6{$_('settings.quick-hour')}</button>
            <button type="button" class="btn btn-sm btn-outline-secondary" onclick={() => setQuickDuration(12 * 60 * 60 * 1000)}>12{$_('settings.quick-hour')}</button>
            <button type="button" class="btn btn-sm btn-outline-secondary" onclick={() => setQuickDuration(24 * 60 * 60 * 1000)}>1{$_('settings.quick-day')}</button>
            <button type="button" class="btn btn-sm btn-outline-secondary" onclick={() => setQuickDuration(3 * 24 * 60 * 60 * 1000)}>3{$_('settings.quick-day')}</button>
            <button type="button" class="btn btn-sm btn-outline-secondary" onclick={() => setQuickDuration(7 * 24 * 60 * 60 * 1000)}>7{$_('settings.quick-day')}</button>
          </div>
        </div>
      </div>

      <hr />

      <!-- Display Mode -->
      <h5 class="mb-3">{$_('settings.display-section')}</h5>

      <div class="row mb-3">
        <label class="col-md-6 col-form-label" for="ct-display-mode">
          {$_('settings.display-mode')}
          <small class="d-block">{$_('settings.display-mode-desc')}</small>
        </label>
        <div class="col-md-6">
          <select class="form-select" id="ct-display-mode" bind:value={config.displayMode}>
            <option value="COVER">{$_('settings.display-cover')}</option>
            <option value="PAGE">{$_('settings.display-page')}</option>
            <option value="SIDEBAR">{$_('settings.display-sidebar')}</option>
          </select>
        </div>
      </div>

      {#if config.displayMode === 'COVER'}
        <div class="alert alert-info">
          <i class="fa fa-info-circle me-2"></i>
          {$_('settings.cover-info')}
        </div>

        <div class="row mb-3">
          <label class="col-md-6 col-form-label" for="ct-show-admin-login">
            {$_('settings.show-admin-login')}
            <small class="d-block">{$_('settings.show-admin-login-desc')}</small>
          </label>
          <div class="col-md-6 d-flex align-items-center">
            <div class="form-check form-switch">
              <input
                class="form-check-input"
                type="checkbox"
                id="ct-show-admin-login"
                bind:checked={config.showAdminLogin} />
            </div>
          </div>
        </div>

        <div class="row mb-3">
          <label class="col-md-6 col-form-label" for="ct-cover-dismissable">
            {$_('settings.cover-dismissable')}
            <small class="d-block">{$_('settings.cover-dismissable-desc')}</small>
          </label>
          <div class="col-md-6 d-flex align-items-center">
            <div class="form-check form-switch">
              <input
                class="form-check-input"
                type="checkbox"
                id="ct-cover-dismissable"
                bind:checked={config.coverDismissable} />
            </div>
          </div>
        </div>

        <div class="row mb-3">
          <label class="col-md-6 col-form-label" for="ct-cover-scope">
            {$_('settings.cover-scope')}
            <small class="d-block">{$_('settings.cover-scope-desc')}</small>
          </label>
          <div class="col-md-6">
            <select class="form-select" id="ct-cover-scope" bind:value={config.coverScope}>
              <option value="ALL">{$_('settings.cover-scope-all')}</option>
              <option value="HOME_ONLY">{$_('settings.cover-scope-home')}</option>
            </select>
          </div>
        </div>
      {/if}

      {#if config.displayMode === 'PAGE'}
        <div class="row mb-3">
          <label class="col-md-6 col-form-label" for="ct-page-path">
            {$_('settings.page-path')}
            <small class="d-block">{$_('settings.page-path-desc')}</small>
          </label>
          <div class="col-md-6">
            <div class="input-group">
              <span class="input-group-text">/</span>
              <input
                class="form-control"
                id="ct-page-path"
                type="text"
                bind:value={pagePathValue}
                placeholder="countdown" />
            </div>
            <small class="text-muted mt-1 d-block">
              <i class="fa fa-link me-1"></i>
              {$_('settings.page-path-info', { values: { url: config.pagePath || '/countdown' } })}
            </small>
          </div>
        </div>

        <div class="row mb-3">
          <label class="col-md-6 col-form-label" for="ct-show-in-nav">
            {$_('settings.show-in-nav')}
            <small class="d-block">{$_('settings.show-in-nav-desc')}</small>
          </label>
          <div class="col-md-6 d-flex align-items-center">
            <div class="form-check form-switch">
              <input
                class="form-check-input"
                type="checkbox"
                id="ct-show-in-nav"
                bind:checked={config.showInNav} />
            </div>
          </div>
        </div>

        {#if config.showInNav}
          <div class="row mb-3">
            <label class="col-md-6 col-form-label" for="ct-nav-link-text">
              {$_('settings.nav-link-text')}
              <small class="d-block">{$_('settings.nav-link-text-desc')}</small>
            </label>
            <div class="col-md-6">
              <input
                class="form-control"
                id="ct-nav-link-text"
                type="text"
                bind:value={config.navLinkText}
                placeholder={config.title || 'Countdown'} />
            </div>
          </div>
        {/if}
      {/if}

      <!-- Appearance / Theme -->
      <h5 class="mb-3">{$_('settings.appearance-section')}</h5>

      <div class="row mb-3">
        <label class="col-md-6 col-form-label" for="ct-theme">
          {$_('settings.theme')}
          <small class="d-block">{$_('settings.theme-desc')}</small>
        </label>
        <div class="col-md-6">
          <select class="form-select" id="ct-theme" bind:value={config.theme}>
            <option value="DARK">{$_('settings.theme-dark')}</option>
            <option value="LIGHT">{$_('settings.theme-light')}</option>
            <option value="BLUR">{$_('settings.theme-blur')}</option>
            <option value="MINIMAL">{$_('settings.theme-minimal')}</option>
            <option value="GRADIENT">{$_('settings.theme-gradient')}</option>
          </select>
        </div>
      </div>

      <div class="row mb-3">
        <label class="col-md-6 col-form-label" for="ct-countdown-style">
          {$_('settings.countdown-style')}
          <small class="d-block">{$_('settings.countdown-style-desc')}</small>
        </label>
        <div class="col-md-6">
          <select class="form-select" id="ct-countdown-style" bind:value={config.countdownStyle}>
            <option value="SIMPLE">{$_('settings.style-simple')}</option>
            <option value="BOXED">{$_('settings.style-boxed')}</option>
            <option value="CIRCULAR">{$_('settings.style-circular')}</option>
            <option value="MINIMAL">{$_('settings.style-minimal')}</option>
            <option value="SEPARATED">{$_('settings.style-separated')}</option>
          </select>
        </div>
      </div>

      <!-- Background Color (for DARK, LIGHT, MINIMAL) -->
      {#if config.theme !== 'GRADIENT' && config.theme !== 'BLUR'}
        <div class="row mb-3">
          <label class="col-md-6 col-form-label" for="ct-bg-color">
            {$_('settings.bg-color')}
          </label>
          <div class="col-md-6">
            <input
              class="form-control form-control-color w-100"
              id="ct-bg-color"
              type="color"
              bind:value={config.backgroundColor} />
          </div>
        </div>
      {/if}

      <!-- Gradient Colors -->
      {#if config.theme === 'GRADIENT'}
        <div class="row mb-3">
          <label class="col-md-6 col-form-label" for="ct-gradient-start">
            {$_('settings.gradient-start')}
          </label>
          <div class="col-md-6">
            <input
              class="form-control form-control-color w-100"
              id="ct-gradient-start"
              type="color"
              bind:value={config.gradientStart} />
          </div>
        </div>
        <div class="row mb-3">
          <label class="col-md-6 col-form-label" for="ct-gradient-end">
            {$_('settings.gradient-end')}
          </label>
          <div class="col-md-6">
            <input
              class="form-control form-control-color w-100"
              id="ct-gradient-end"
              type="color"
              bind:value={config.gradientEnd} />
          </div>
        </div>
      {/if}

      <!-- Text Color -->
      <div class="row mb-3">
        <label class="col-md-6 col-form-label" for="ct-text-color">
          {$_('settings.text-color')}
        </label>
        <div class="col-md-6">
          <input
            class="form-control form-control-color w-100"
            id="ct-text-color"
            type="color"
            bind:value={config.textColor} />
        </div>
      </div>

      <!-- Background Image -->
      <div class="row mb-3">
        <label class="col-md-6 col-form-label">
          {$_('settings.bg-image')}
          <small class="d-block">{$_('settings.bg-image-desc')}</small>
        </label>
        <div class="col-md-6">
          {#if config.backgroundImage}
            <div class="mb-2">
              <img
                src="/api/countdown-timer/background/{config.backgroundImage}"
                alt="Background"
                class="img-thumbnail"
                style="max-height: 120px; object-fit: cover;" />
            </div>
            <button class="btn btn-sm btn-outline-danger" onclick={deleteBackground} disabled={uploadingBg}>
              <i class="fa fa-trash me-1"></i>
              {$_('settings.bg-image-remove')}
            </button>
          {:else}
            <input
              class="form-control"
              type="file"
              accept="image/png,image/jpeg,image/webp,image/gif"
              onchange={uploadBackground}
              disabled={uploadingBg} />
            {#if uploadingBg}
              <div class="mt-2">
                <span class="spinner-border spinner-border-sm me-1" role="status" aria-hidden="true"></span>
                {$_('settings.bg-image-uploading')}
              </div>
            {/if}
          {/if}
        </div>
      </div>

      <hr />

      <!-- Server Info Section -->
      <h5 class="mb-3">{$_('settings.server-info-section')}</h5>

      <div class="row mb-3">
        <label class="col-md-6 col-form-label" for="ct-show-logo">
          {$_('settings.show-logo')}
          <small class="d-block">{$_('settings.show-logo-desc')}</small>
        </label>
        <div class="col-md-6 d-flex align-items-center">
          <div class="form-check form-switch">
            <input
              class="form-check-input"
              type="checkbox"
              id="ct-show-logo"
              bind:checked={config.showLogo} />
          </div>
        </div>
      </div>

      {#if config.showLogo}
        <div class="row mb-3">
          <label class="col-md-6 col-form-label" for="ct-logo-size">
            {$_('settings.logo-size')}
            <small class="d-block">{$_('settings.logo-size-desc')}</small>
          </label>
          <div class="col-md-6">
            <select class="form-select" id="ct-logo-size" bind:value={config.logoSize}>
              <option value="SMALL">{$_('settings.logo-size-small')} (64px)</option>
              <option value="MEDIUM">{$_('settings.logo-size-medium')} (128px)</option>
              <option value="LARGE">{$_('settings.logo-size-large')} (192px)</option>
              <option value="EXTRA_LARGE">{$_('settings.logo-size-xl')} (256px)</option>
              <option value="HUGE">{$_('settings.logo-size-huge')} (384px)</option>
              <option value="MASSIVE">{$_('settings.logo-size-massive')} (512px)</option>
            </select>
          </div>
        </div>
      {/if}

      <div class="row mb-3">
        <label class="col-md-6 col-form-label">
          {$_('settings.show-server-ip')}
          <small class="d-block">{$_('settings.show-server-ip-desc')}</small>
        </label>
        <div class="col-md-6 d-flex align-items-center">
          <div class="form-check form-switch">
            <input
              class="form-check-input"
              type="checkbox"
              id="ct-show-server-ip"
              bind:checked={config.showServerIp} />
          </div>
        </div>
      </div>

      <hr />

      <!-- Opening Effect -->
      <h5 class="mb-3">{$_('settings.effects-section')}</h5>

      <div class="row mb-3">
        <label class="col-md-6 col-form-label" for="ct-opening-effect">
          {$_('settings.opening-effect')}
          <small class="d-block">{$_('settings.opening-effect-desc')}</small>
        </label>
        <div class="col-md-6">
          <select class="form-select" id="ct-opening-effect" bind:value={config.openingEffect}>
            <option value="NONE">{$_('settings.effect-none')}</option>
            <option value="CONFETTI">{$_('settings.effect-confetti')}</option>
            <option value="FIREWORKS">{$_('settings.effect-fireworks')}</option>
            <option value="SPARKLE">{$_('settings.effect-sparkle')}</option>
          </select>
        </div>
      </div>

      <hr />

      <!-- Actions when timer ends -->
      <h5 class="mb-3">{$_('settings.actions-section')}</h5>

      <div class="row mb-3">
        <label class="col-md-6 col-form-label">
          {$_('settings.action-hide')}
          <small class="d-block">{$_('settings.action-hide-desc')}</small>
        </label>
        <div class="col-md-6 d-flex align-items-center">
          <div class="form-check form-switch">
            <input
              class="form-check-input"
              type="checkbox"
              id="ct-action-hide"
              checked={config.actions?.includes('HIDE')}
              onchange={toggleAction('HIDE')} />
          </div>
        </div>
      </div>



      <div class="mt-4">
        <button class="btn btn-secondary" onclick={saveConfig} disabled={saving || !hasChanges}>
          {#if saving}
            <span class="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"
            ></span>
          {/if}
          {$_('generic.save')}
        </button>
      </div>
    </div>
  </div>
{/if}

<script>
  import {_} from '../../../main';
  import {showToast} from '@panomc/sdk/toasts';
  import ApiUtil from '@panomc/sdk/utils/api';

  export let addon;

  let config = addon?.config || {
    enabled: false,
    endDate: 0,
    title: '',
    message: '',
    displayMode: 'COVER',
    theme: 'DARK',
    backgroundColor: '#000000',
    textColor: '#ffffff',
    gradientStart: '#667eea',
    gradientEnd: '#764ba2',
    backgroundImage: '',
    showLogo: true,
    logoSize: 'MEDIUM',
    showServerIp: false,
    countdownStyle: 'SIMPLE',
    actions: ['HIDE'],
    openingEffect: 'NONE',
    pagePath: '/countdown',
    showInNav: true,
    navLinkText: '',
    showAdminLogin: true,
    coverDismissable: false,
    coverScope: 'ALL',
  };
  let saving = false;
  let uploadingBg = false;

  let initialConfig = JSON.parse(JSON.stringify(config));

  $: hasChanges = JSON.stringify(config) !== JSON.stringify(initialConfig);

  // Page path helper: strip leading slash for input, add it back for config
  let pagePathValue = (config.pagePath || '/countdown').replace(/^\//, '');
  $: config.pagePath = '/' + pagePathValue.replace(/^\//, '');

  // Convert epoch millis to local datetime-local input value
  function epochToLocal(epoch) {
    if (!epoch || epoch === 0) return '';
    const d = new Date(epoch);
    const pad = (n) => String(n).padStart(2, '0');
    return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}T${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`;
  }

  function localToEpoch(localStr) {
    if (!localStr) return 0;
    return new Date(localStr).getTime();
  }

  let endDateLocal = epochToLocal(config.endDate);

  $: config.endDate = localToEpoch(endDateLocal);

  function setQuickDuration(ms) {
    const target = Date.now() + ms;
    config.endDate = target;
    endDateLocal = epochToLocal(target);
  }

  function toggleAction(action) {
    return (e) => {
      if (!config.actions) config.actions = [];
      if (e.target.checked) {
        if (!config.actions.includes(action)) {
          config.actions = [...config.actions, action];
        }
      } else {
        config.actions = config.actions.filter((a) => a !== action);
      }
    };
  }

  async function uploadBackground(e) {
    const file = e.target.files[0];
    if (!file) return;

    uploadingBg = true;
    try {
      const formData = new FormData();
      formData.append('file', file);

      const result = await ApiUtil.post({
        path: '/api/panel/countdown-timer/background',
        body: formData,
        headers: {},
      });
      config.backgroundImage = result.backgroundImage;
      initialConfig.backgroundImage = result.backgroundImage;
      await showToast(`plugins.${addon.id}.toasts.bg-upload-success`);
    } catch (err) {
      console.error('Failed to upload background', err);
      await showToast(`plugins.${addon.id}.toasts.bg-upload-error`);
    } finally {
      uploadingBg = false;
    }
  }

  async function deleteBackground() {
    uploadingBg = true;
    try {
      await ApiUtil.delete({
        path: '/api/panel/countdown-timer/background',
      });
      config.backgroundImage = '';
      initialConfig.backgroundImage = '';
      await showToast(`plugins.${addon.id}.toasts.bg-delete-success`);
    } catch (err) {
      console.error('Failed to delete background', err);
      await showToast(`plugins.${addon.id}.toasts.bg-delete-error`);
    } finally {
      uploadingBg = false;
    }
  }

  async function saveConfig() {
    if (saving) return;
    saving = true;
    try {
      await ApiUtil.put({
        path: '/api/panel/countdown-timer/config',
        body: config,
      });
      addon.config = config;
      initialConfig = JSON.parse(JSON.stringify(config));
      await showToast(`plugins.${addon.id}.toasts.save-success`);
    } catch (e) {
      console.error('Failed to save countdown timer config', e);
      await showToast(`plugins.${addon.id}.toasts.save-error`);
    } finally {
      saving = false;
    }
  }
</script>
